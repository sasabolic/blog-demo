package io.sixhours.blog.demo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.util.Map;

/**
 * Serializer which converts Object to byte array using Avro.
 *
 * @author Sasa Bolic
 */
public class AvroEventSerializer implements Serializer<Event> {

    /**
     * Avro schema used to serialize data.
     */
    public static final Schema SCHEMA = SchemaBuilder
            .record("Event").namespace("io.sixhours.blog.demo")
            .fields()
            .name("class").type().stringType().noDefault()
            .name("payload").type().stringType().noDefault()
            .endRecord();

    private Injection<GenericRecord, byte[]> recordInjection;
    private GenericData.Record genericRecord;
    private ObjectMapper mapper;

    /**
     * Instantiates a new {@code AvroEventSerializer}.
     */
    public AvroEventSerializer() {
        recordInjection = GenericAvroCodecs.toBinary(SCHEMA);
        genericRecord = new GenericData.Record(SCHEMA);

        mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // do nothing
    }

    @Override
    public byte[] serialize(String topic, Event data) {
        if (data == null) {
            return null;
        }

        genericRecord.put("class", data.getClass().getCanonicalName());

        try {
            genericRecord.put("payload", mapper.writeValueAsString(data));

            return recordInjection.apply(genericRecord);
        } catch (IOException e) {
            throw new RuntimeException("Could not serialize data", e);
        }
    }

    @Override
    public void close() {
        // do nothing
    }
}