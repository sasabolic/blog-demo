package io.sixhours.blog.demo.blogdemo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    enum TopicType {
        POST("post", "{\"namespace\": \"io.sixhours.blog.demo.blogdemo\",\n" +
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

        private final String topicName;
        private final String schema;

        TopicType(String topicName, String schema) {
            this.topicName = topicName;
            this.schema = schema;
        }

        public String topicName() {
            return this.topicName;
        }

        public String schema() {
            return this.schema;
        }
    }

    private Producer<String, byte[]> producer;
    private EventHandler handler = createFlow();

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

    public void sendEvent(TopicType topic, String key, Event value) {

        final byte[] bytes = handler.handleRequest(value);

        ProducerRecord<String, byte[]> rec =
                new ProducerRecord<>(value.topicName, key, bytes);

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

    private static EventHandler createFlow() {
        EventHandler blogPostDeletedHandler = new BlogPostDeletedHandler();
        EventHandler blogPostCreatedHandler = new BlogPostCreatedHandler();

        blogPostDeletedHandler.setNext(blogPostCreatedHandler);

        return blogPostDeletedHandler;
    }
}
