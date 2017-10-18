package io.sixhours.blog.demo.blogdemo;

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

    public AvroService() {

    }

    public AvroService(String schemaLocation) {
        Path path = null;
        try {
            path = Paths.get(getClass().getClassLoader().getResource(schemaLocation).toURI());

            StringBuilder data = new StringBuilder();
            Stream<String> lines = Files.lines(path);
            lines.forEach(line -> data.append(line).append("\n"));
            lines.close();

            initialize(data.toString().trim());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize(String schemaS) {
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(schemaS);

        recordInjection = GenericAvroCodecs.toBinary(schema);

        record =  new GenericData.Record(schema);
    }

    public GenericRecord getRecord() {
        return record;
    }

    byte[] getData() {
        return recordInjection.apply(record);
    }
}
