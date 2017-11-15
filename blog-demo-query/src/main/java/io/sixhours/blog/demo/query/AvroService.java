package io.sixhours.blog.demo.query;

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
import java.util.Map;
import java.util.stream.Stream;

/**
 * Service used to serialize data with Avro.
 *
 * @author Sasa Bolic
 */
public class AvroService {

    Schema getSchema(String schemaLocation) {
        try {
            Path path = Paths.get(AvroService.class.getClassLoader().getResource(schemaLocation).toURI());

            StringBuilder data = new StringBuilder();
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> data.append(line).append("\n"));
            lines.close();

            Schema.Parser parser = new Schema.Parser();
            return parser.parse(data.toString().trim());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Could not create AvroService", e);
        }
    }

    /**
     * Returns serialized data as byte array.
     *
     * @param data the data
     * @return the byte [ ]
     */
    byte[] encode(Map<String, Object> data, Schema schema) {
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);
        GenericData.Record record = new GenericData.Record(schema);

        data.forEach((k, v) -> record.put(k, v));

        return recordInjection.apply(record);
    }

    /**
     * Deserializes data from byte[] to {@link GenericRecord}.
     *
     * @param value the value
     * @return the generic record
     */
    GenericRecord decode(byte[] value, Schema schema) {
        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);

        return recordInjection.invert(value).get();
    }
}
