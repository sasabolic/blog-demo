package io.sixhours.blog.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Handles {@link BlogPostCreated} events.
 *
 * @author Sasa Bolic
 */
public class BlogPostCreatedHandler implements EventHandler {
    private static final Logger log = LoggerFactory.getLogger(BlogPostCreatedHandler.class);

    private EventHandler next;
    private AvroService avroService = new AvroService("schemas/blog_post_created.avsc");

    @Override
    public void setNext(EventHandler handler) {
        this.next = Objects.requireNonNull(handler, "Handler cannot be null");
    }

    @Override
    public byte[] handleRequest(Event event) {
        Objects.requireNonNull(event, "Event not be null");

        if (event instanceof BlogPostCreated) {
            log.debug("Event BlogPostCreated: '{}'", ((BlogPostCreated) event).getAggregateId());

            avroService.addField("aggregate_id", ((BlogPostCreated) event).getAggregateId().toString());
            avroService.addField("title", ((BlogPostCreated) event).getTitle());
            avroService.addField("body", ((BlogPostCreated) event).getBody());
            avroService.addField("author", ((BlogPostCreated) event).getAuthor());
            avroService.addField("date_created", ((BlogPostCreated) event).getDateCreated().getTime());

            return avroService.getData();
        } else if (this.next != null) {
            return this.next.handleRequest(event);
        } else {
            throw new RuntimeException("Event is left unhandled");
        }
    }

}