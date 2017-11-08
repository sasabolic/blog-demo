package io.sixhours.blog.demo.command;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Service used to serialize data with Avro.
 *
 * @author Sasa Bolic
 */
public class AvroService {

    private Injection<GenericRecord, byte[]> recordInjection;
    private GenericRecord record;

    /**
     * Instantiates a new {@code AvroService}.
     *
     * @param schemaLocation the schema location
     */
    public AvroService(String schemaLocation) {
        try {
            Path path = Paths.get(AvroService.class.getClassLoader().getResource(schemaLocation).toURI());

            StringBuilder data = new StringBuilder();
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> data.append(line).append("\n"));
            lines.close();

            Schema.Parser parser = new Schema.Parser();
            Schema schema = parser.parse(data.toString().trim());

            recordInjection = GenericAvroCodecs.toBinary(schema);

            record =  new GenericData.Record(schema);

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Could not create AvroService", e);
        }
    }


    /**
     * Adds field to Avro record.
     *
     * @param key   the key
     * @param value the value
     */
    public void addField(String key, Object value) {
        getRecord().put(key, value);
    }

    /**
     * Returns serialized data as byte array.
     *
     * @return the byte [ ]
     */
    byte[] getData() {
        return getRecordInjection().apply(getRecord());
    }

    /**
     * Returns record {@link Injection}.
     *
     * @return the record injection
     */
    public Injection<GenericRecord, byte[]> getRecordInjection() {
        return recordInjection;
    }

    /**
     * Returns  {@link GenericRecord}.
     *
     * @return the record
     */
    public GenericRecord getRecord() {
        return record;
    }
}
