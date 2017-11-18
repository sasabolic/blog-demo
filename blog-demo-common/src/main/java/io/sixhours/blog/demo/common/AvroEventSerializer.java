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

public class AvroEventSerializer implements Serializer<Event> {

    public static final Schema SCHEMA = SchemaBuilder
            .record("Event").namespace("io.sixhours.blog.demo")
            .fields()
            .name("class").type().stringType().noDefault()
            .name("payload").type().stringType().noDefault()
            .endRecord();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Event data) {
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(SCHEMA);
        GenericData.Record genericRecord = new GenericData.Record(SCHEMA);

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        genericRecord.put("class", data.getClass().getCanonicalName());

        try {
            genericRecord.put("payload", mapper.writeValueAsString(data));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recordInjection.apply(genericRecord);

    }

    @Override
    public void close() {

    }
}