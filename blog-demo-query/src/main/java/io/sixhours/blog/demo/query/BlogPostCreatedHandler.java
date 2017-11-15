package io.sixhours.blog.demo.query;

import io.sixhours.blog.demo.common.BlogPostCreated;
import io.sixhours.blog.demo.common.Event;
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
    private BlogPostUpdateService updateService = new BlogPostUpdateService();

    @Override
    public void setNext(EventHandler handler) {
        this.next = Objects.requireNonNull(handler, "Handler cannot be null");
    }

    @Override
    public void handle(Event event) {
        Objects.requireNonNull(event, "Event not be null");

        if (event instanceof BlogPostCreated) {
            updateService.create(BlogPostCreated.class.cast(event));
        } else if (this.next != null) {
            this.next.handle(event);
        } else {
            throw new RuntimeException("Event is left unhandled");
        }
    }

}