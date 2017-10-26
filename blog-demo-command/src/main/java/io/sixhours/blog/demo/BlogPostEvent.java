package io.sixhours.blog.demo;

import java.time.ZonedDateTime;
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
    public BlogPostEvent(UUID aggregateId, ZonedDateTime date) {
        super(aggregateId, date, "post");
    }
}
