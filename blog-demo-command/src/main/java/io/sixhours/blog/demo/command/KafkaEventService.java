package io.sixhours.blog.demo.command;

import io.sixhours.blog.demo.common.AvroEventSerializer;
import io.sixhours.blog.demo.common.Event;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Event service using Kafka to send events.
 *
 * @author Sasa Bolic
 */
@Service
public class KafkaEventService implements EventService, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventService.class);

    private Producer<String, Event> producer;
    private Properties properties = new Properties();

    /**
     * Instantiates a new {@code KafkaEventService}.
     */
    public KafkaEventService() {
        this("localhost:9092");

    }

    /**
     * Instantiates a new {@code KafkaEventService}.
     *
     * @param bootstrapServers the list of bootstrap servers
     */
    public KafkaEventService(String bootstrapServers) {
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "blog-demo-producer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroEventSerializer.class.getName());
        properties.put(ProducerConfig.ACKS_CONFIG, "all");

        producer = new KafkaProducer<>(properties);
    }

    /**
     * Returns {@link Producer}.
     *
     * @return the producer
     */
    Producer<String, Event> getProducer() {
        return this.producer;
    }

    public void sendEvent(Event value) {
        final ProducerRecord<String, Event> rec = new ProducerRecord<>(value.getTopicName(), value.getAggregateId().toString(), value);

        try {
            final RecordMetadata m = getProducer().send(rec).get();

            log.info("Message produced with topic: {}, partition: {}, offset: {}", m.topic(), m.partition(), m.offset());

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            producer.flush();
        }
    }

    @Override
    public void destroy() throws Exception {
        if (producer != null) {
            producer.close();
        }
    }
}
