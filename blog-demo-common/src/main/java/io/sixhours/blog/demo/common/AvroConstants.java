package io.sixhours.blog.demo.common;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 * Avro constants.
 *
 * @author Sasa Bolic
 */
public final class AvroConstants {

    /**
     * Avro schema used to serialize data.
     */
    public static final Schema SCHEMA = SchemaBuilder
            .record("Event").namespace("io.sixhours.blog.demo.common")
            .fields()
            .name("class").type().stringType().noDefault()
            .name("payload").type().stringType().noDefault()
            .endRecord();
}
