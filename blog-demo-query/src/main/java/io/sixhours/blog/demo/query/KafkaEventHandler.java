package io.sixhours.blog.demo.query;

import io.sixhours.blog.demo.common.*;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class KafkaEventHandler {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventHandler.class);

    private AvroService avroService = new AvroService();
    private EventHandler handler = createFlow();

    public void handle(ConsumerRecord<String, byte[]> r) {
        String schema = new String(r.headers().lastHeader("schema").value());
        String className = new String(r.headers().lastHeader("class").value());

        GenericRecord record = avroService.decode(r.value(), avroService.getSchema("schemas/" + schema));

        try {
            final Class<? extends BlogPostEvent> eventClass = (Class<? extends BlogPostEvent>) Class.forName(className);
            Event event = eventClass.getConstructor(GenericRecord.class).newInstance(record);

            handler.handle(event);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static EventHandler createFlow() {
        EventHandler blogPostDeletedHandler = new BlogPostDeletedHandler();
        EventHandler blogPostCreatedHandler = new BlogPostCreatedHandler();
        EventHandler blogPostUpdatedHandler = new BlogPostUpdatedHandler();

        blogPostDeletedHandler.setNext(blogPostCreatedHandler);
        blogPostCreatedHandler.setNext(blogPostUpdatedHandler);

        return blogPostDeletedHandler;
    }

}
