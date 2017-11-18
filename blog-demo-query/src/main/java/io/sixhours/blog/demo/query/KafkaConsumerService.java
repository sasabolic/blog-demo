package io.sixhours.blog.demo.query;

import io.sixhours.blog.demo.common.AvroEventDeserializer;
import io.sixhours.blog.demo.common.Event;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class KafkaConsumerService implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    private Consumer<String, Event> consumer;
    private final List<String> topics;
    private final boolean loop;
    private EventHandler handler = createFlow();

    public KafkaConsumerService(String groupId, List<String> topics) {
        this(groupId, topics, true);
    }

    public KafkaConsumerService(String groupId, List<String> topics, boolean loop) {
        this.topics = topics;
        this.loop = loop;

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroEventDeserializer.class.getName());

        consumer = new KafkaConsumer<>(props);
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);

            do {
                log.info("Consumer polling...");
                ConsumerRecords<String, Event> records = consumer.poll(Long.MAX_VALUE);
                records.forEach(r -> {
                        log.info("record = {}", r);
                        try {
                            handler.handle(r.value());
                        } catch (Exception e) {
                            log.error("Error occurred while handling event: {}", r.value().getAggregateId());
                        }
                });
            } while (loop);
        } catch (WakeupException e) {
            // ignore for shutdown
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        log.info("Consumer shutdown...");
        consumer.wakeup();
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