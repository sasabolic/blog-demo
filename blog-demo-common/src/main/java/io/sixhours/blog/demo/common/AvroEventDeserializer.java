package io.sixhours.blog.demo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

import static io.sixhours.blog.demo.common.AvroConstants.SCHEMA;

/**
 * Deserializer which converts byte array to Object using Avro.
 *
 * @author Sasa Bolic
 */
public class AvroEventDeserializer implements Deserializer<Event> {
    private static final Logger log = LoggerFactory.getLogger(AvroEventDeserializer.class);

    private Injection<GenericRecord, byte[]> recordInjection;
    private ObjectMapper mapper;

    /**
     * Instantiates a new {@code AvroEventDeserializer}.
     */
    public AvroEventDeserializer() {
        recordInjection = GenericAvroCodecs.toBinary(SCHEMA);

        mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Event deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        GenericRecord genericRecord = recordInjection.invert(data).get();

        try {
            final Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName(String.valueOf(genericRecord.get("class")));

            String payload = String.valueOf(genericRecord.get("payload"));

            log.info("Payload: '{}'", payload);

            return mapper.readValue(payload, eventClass);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("Could not deserialize data", e);
        }
    }

    @Override
    public void close() {

    }
}
