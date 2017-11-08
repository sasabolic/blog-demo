package io.sixhours.blog.demo.command;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Event service using Kafka to send events.
 *
 * @author Sasa Bolic
 */
public class KafkaEventService implements EventService {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventService.class);

    private Producer<String, byte[]> producer;
    private EventHandler handler = createFlow();

    /**
     * Instantiates a new {@code KafkaEventService}.
     */
    public KafkaEventService() {
        Properties p = new Properties();

        p.put("bootstrap.servers", "localhost:9092");
        p.put("client.id", "blog-demo-producer");
        p.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        p.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        producer = new KafkaProducer<>(p);

    }

    /**
     * Returns {@link Producer}.
     *
     * @return the producer
     */
    Producer<String, byte[]> getProducer() {
        return this.producer;
    }

    public void sendEvent(Event value) {

        final ProducerRecord<String, byte[]> rec = handler.encode(value);

        try {
            final RecordMetadata m = getProducer().send(rec).get();

            log.info("Message produced with topic: {}, partition: {}, offset: {}", m.topic(), m.partition(), m.offset());

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            if (getProducer() != null) {
//                getProducer().close();
            }
        }
    }

    private static EventHandler createFlow() {
        EventHandler blogPostDeletedHandler = new BlogPostDeletedHandler();
        EventHandler blogPostCreatedHandler = new BlogPostCreatedHandler();
        EventHandler blogPostUpdatedHandler = new BlogPostUpdatedHandler();

        blogPostDeletedHandler.setNext(blogPostCreatedHandler);
        blogPostCreatedHandler.setNext(blogPostUpdatedHandler);

        return blogPostDeletedHandler;
    }
}
