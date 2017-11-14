package io.sixhours.blog.demo.command;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;

/**
 * Handles {@link BlogPostUpdated} events.
 *
 * @author Sasa Bolic
 */
public class BlogPostUpdatedHandler implements EventHandler {
    private static final Logger log = LoggerFactory.getLogger(BlogPostUpdatedHandler.class);

    private EventHandler next;
    private static final String SCHEMA = "blog_post_updated.avsc";
    private AvroService avroService = new AvroService("schemas/" + SCHEMA);

    @Override
    public void setNext(EventHandler handler) {
        this.next = Objects.requireNonNull(handler, "Handler cannot be null");
    }

    @Override
    public ProducerRecord encode(Event event) {
        Objects.requireNonNull(event, "Event not be null");

        if (event instanceof BlogPostUpdated) {
            log.debug("Event BlogPostUpdated: '{}'", ((BlogPostUpdated) event).getAggregateId());

            Map<String, Object> data = Map.ofEntries(
                    entry("aggregate_id", ((BlogPostUpdated) event).getAggregateId().toString()),
                    entry("title", ((BlogPostUpdated) event).getTitle()),
                    entry("body", ((BlogPostUpdated) event).getBody()),
                    entry("author", ((BlogPostUpdated) event).getAuthor())
            );

            ProducerRecord<String, byte[]> rec =
                    new ProducerRecord<>(event.topicName, event.aggregateId.toString(), avroService.encode(data));

            rec.headers().add("schema", SCHEMA.getBytes());

            return rec;
        } else if (this.next != null) {
            return this.next.encode(event);
        } else {
            throw new RuntimeException("Event is left unhandled");
        }
    }

}