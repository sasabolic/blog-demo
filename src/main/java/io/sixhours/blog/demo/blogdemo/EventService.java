package io.sixhours.blog.demo.blogdemo;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.avro.Schema;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    enum TopicType {
        POST("post");

        private final String topicName;

        TopicType(String topicName) {
            this.topicName = topicName;
        }

        public String topicName() {
            return this.topicName;
        }
    }

    private Producer<String, byte[]> producer;

    public EventService() {
        Properties p = new Properties();

        p.put("bootstrap.servers", "localhost:9092");
        p.put("client.id", "blog-demo-producer");
        p.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        p.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        producer = new KafkaProducer<>(p);

    }

    Producer<String, byte[]> getProducer() {
        return this.producer;
    }

    public void sendEvent(TopicType topic, String key, Object value) {

        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse("{\"namespace\": \"io.sixhours.blog.demo.blogdemo\",\n" +
                " \"type\": \"record\",\n" +
                " \"name\": \"BlogPost\",\n" +
                " \"fields\": [\n" +
                "     {\"name\": \"id\", \"type\": \"string\"},\n" +
                "     {\"name\": \"title\", \"type\": \"string\"},\n" +
                "     {\"name\": \"body\", \"type\": \"string\"},\n" +
                "     {\"name\": \"author\", \"type\": \"string\"},\n" +
                "     {\"name\": \"date_created\",  \"type\": {\"type\": \"long\", \"logicalType\": \"timestamp-millis\"}}\n" +
                " ]\n" +
                "}");

        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);

        GenericRecord data = new GenericData.Record(schema);
        final BlogPostCreated event = (BlogPostCreated) value;

        data.put("id", event.getId().toString());
        data.put("title", event.getTitle());
        data.put("body", event.getBody());
        data.put("author", event.getAuthor());
        data.put("date_created", event.getDateCreated().getTime());

        byte[] bytes = recordInjection.apply(data);

        ProducerRecord<String, byte[]> rec =
                new ProducerRecord<>(topic.topicName(), key, bytes);

        try {
            final RecordMetadata m = getProducer().send(rec).get();

            log.info("Message produced with topic: {}, partition: {}, offset: {}", m.topic(), m.partition(), m.offset());

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            if (getProducer() != null) {
                getProducer().close();
            }
        }
    }
}
