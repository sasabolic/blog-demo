package io.sixhours.blog.demo;

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

public class AvroService {

    private Injection<GenericRecord, byte[]> recordInjection;
    private GenericRecord record;

    public AvroService(String schemaLocation) {
        try {
            Path path = Paths.get(getClass().getClassLoader().getResource(schemaLocation).toURI());

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


    public void addField(String key, Object value) {
        getRecord().put(key, value);
    }

    byte[] getData() {
        return getRecordInjection().apply(getRecord());
    }

    public Injection<GenericRecord, byte[]> getRecordInjection() {
        return recordInjection;
    }

    public GenericRecord getRecord() {
        return record;
    }
}
