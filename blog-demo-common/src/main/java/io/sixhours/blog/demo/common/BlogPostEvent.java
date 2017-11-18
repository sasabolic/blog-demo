package io.sixhours.blog.demo.common;

import java.time.Instant;
import java.util.UUID;

/**
 * Blog post event.
 *
 * @author Sasa Bolic
 */
public abstract class BlogPostEvent extends Event {

    /**
     * Instantiates a new {@code BlogPostEvent}.
     *
     * @param aggregateId the aggregate id
     * @param date        the date
     */
    public BlogPostEvent(UUID aggregateId, Instant date) {
        super(aggregateId, date, "blog-demo.post");
    }

    public BlogPostEvent() {
        super();
    }
}
