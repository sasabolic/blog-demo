package io.sixhours.blog.demo.blogdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * The type used to handle {@link BlogPostDeleted} events.
 *
 * @author Sasa Bolic
 */
public class BlogPostDeletedHandler implements EventHandler {
    private static final Logger log = LoggerFactory.getLogger(BlogPostDeletedHandler.class);

    private EventHandler next;
    private AvroService avroService = new AvroService("schemas/blog_post_deleted.avsc");

    @Override
    public void setNext(EventHandler handler) {
        this.next = Objects.requireNonNull(handler, "Handler cannot be null");
    }

    @Override
    public byte[] handleRequest(Event event) {
        Objects.requireNonNull(event, "Event must not be null");

        if (event instanceof BlogPostDeleted) {
            log.debug("Event BlogPostDeleted: '{}'", ((BlogPostDeleted) event).getAggregateId());

            avroService.getRecord().put("aggregate_id", ((BlogPostDeleted) event).getAggregateId().toString());

            return avroService.getData();
        } else if (this.next != null) {
            return this.next.handleRequest(event);
        } else {
            throw new RuntimeException("Event is left unhandled");
        }
    }

}