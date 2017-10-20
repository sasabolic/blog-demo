package io.sixhours.blog.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * The type used to handle {@link BlogPostUpdated} events.
 *
 * @author Sasa Bolic
 */
public class BlogPostUpdatedHandler implements EventHandler {
    private static final Logger log = LoggerFactory.getLogger(BlogPostUpdatedHandler.class);

    private EventHandler next;
    private AvroService avroService = new AvroService("schemas/blog_post_updated.avsc");

    @Override
    public void setNext(EventHandler handler) {
        this.next = Objects.requireNonNull(handler, "Handler cannot be null");
    }

    @Override
    public byte[] handleRequest(Event event) {
        Objects.requireNonNull(event, "Event not be null");

        if (event instanceof BlogPostUpdated) {
            log.debug("Event BlogPostUpdated: '{}'", ((BlogPostUpdated) event).getAggregateId());

            avroService.addField("aggregate_id", ((BlogPostUpdated) event).getAggregateId().toString());
            avroService.addField("title", ((BlogPostUpdated) event).getTitle());
            avroService.addField("body", ((BlogPostUpdated) event).getBody());
            avroService.addField("author", ((BlogPostUpdated) event).getAuthor());

            return avroService.getData();
        } else if (this.next != null) {
            return this.next.handleRequest(event);
        } else {
            throw new RuntimeException("Event is left unhandled");
        }
    }

}