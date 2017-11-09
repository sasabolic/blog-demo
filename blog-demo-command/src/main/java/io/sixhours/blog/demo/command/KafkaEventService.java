package io.sixhours.blog.demo.command;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
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
        this("localhost:9092");

    }

    public KafkaEventService(String bootStrapServers) {
        createProducer(bootStrapServers);
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

    private void createProducer(String bootStrapServer) {
        long start = System.currentTimeMillis();
        Properties p = new Properties();

        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        p.put(ProducerConfig.CLIENT_ID_CONFIG, "blog-demo-producer");
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        producer = new KafkaProducer<>(p);
        log.info("SALE PRODUCER CREATED in {}ms", System.currentTimeMillis() - start);
    }

}
