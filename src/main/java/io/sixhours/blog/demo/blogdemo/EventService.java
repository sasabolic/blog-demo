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
        POST("post");

        private final String topicName;

        TopicType(String topicName) {
            this.topicName = topicName;
        }

        public String topicName() {
            return this.topicName;
        }
    }

    private Producer<String, String> producer;

    public EventService() {
        Properties p = new Properties();

        p.put("bootstrap.servers","localhost:9092");
        p.put("client.id", "blog-demo-producer");
        p.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        p.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(p);

    }

    Producer<String, String> getProducer() {
        return this.producer;
    }

    public void sendEvent(TopicType topic, String key, String value) {
        ProducerRecord<String,String> rec = new ProducerRecord<>(topic.topicName(), key, value);

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
