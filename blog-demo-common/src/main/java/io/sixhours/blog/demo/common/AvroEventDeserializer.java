package io.sixhours.blog.demo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class AvroEventDeserializer implements Deserializer<Event> {
    private static final Logger log = LoggerFactory.getLogger(AvroEventDeserializer.class);

    public static final Schema SCHEMA = SchemaBuilder
            .record("Event").namespace("io.sixhours.blog.demo.common")
            .fields()
            .name("class").type().stringType().noDefault()
            .name("payload").type().stringType().noDefault()
            .endRecord();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Event deserialize(String topic, byte[] data) {
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(SCHEMA);

        GenericRecord genericRecord = recordInjection.invert(data).get();

        try {
            final Class<? extends BlogPostEvent> eventClass = (Class<? extends BlogPostEvent>) Class.forName(String.valueOf(genericRecord.get("class")));

            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());

            String payload = String.valueOf(genericRecord.get("payload"));

            log.info("Payload: '{}'", payload);
            return mapper.readValue(payload, eventClass);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {

    }
}
