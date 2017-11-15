package io.sixhours.blog.demo.command;

import io.sixhours.blog.demo.common.BlogPostCreated;
import io.sixhours.blog.demo.common.Event;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;

/**
 * Handles {@link BlogPostCreated} events.
 *
 * @author Sasa Bolic
 */
public class BlogPostCreatedHandler implements EventHandler {
    private static final Logger log = LoggerFactory.getLogger(BlogPostCreatedHandler.class);

    private EventHandler next;
    private static final String SCHEMA = "blog_post_created.avsc";
    private AvroService avroService = new AvroService("schemas/" + SCHEMA);

    @Override
    public void setNext(EventHandler handler) {
        this.next = Objects.requireNonNull(handler, "Handler cannot be null");
    }

    @Override
    public ProducerRecord encode(Event event) {
        Objects.requireNonNull(event, "Event not be null");

        if (event instanceof BlogPostCreated) {
            log.debug("Event BlogPostCreated: '{}'", ((BlogPostCreated) event).getAggregateId());

            Map<String, Object> data = Map.ofEntries(
                    entry("aggregate_id", ((BlogPostCreated) event).getAggregateId().toString()),
                    entry("title", ((BlogPostCreated) event).getTitle()),
                    entry("body", ((BlogPostCreated) event).getBody()),
                    entry("author", ((BlogPostCreated) event).getAuthor()),
                    entry("date_created", ((BlogPostCreated) event).getDateCreated().toString())
//            entry("date_created", ((BlogPostCreated) event).getDateCreated().toInstant().toString())
            );

            ProducerRecord<String, byte[]> rec =
                    new ProducerRecord<>(event.getTopicName(), event.getAggregateId().toString(), avroService.encode(data));

            rec.headers().add("schema", SCHEMA.getBytes());
            rec.headers().add("class", BlogPostCreated.class.getCanonicalName().getBytes());

            return rec;
        } else if (this.next != null) {
            return this.next.encode(event);
        } else {
            throw new RuntimeException("Event is left unhandled");
        }
    }

}