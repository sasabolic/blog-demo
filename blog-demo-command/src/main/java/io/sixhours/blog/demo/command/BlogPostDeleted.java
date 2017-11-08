package io.sixhours.blog.demo.command;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Event triggered when blog post is deleted.
 *
 * @author Sasa Bolic
 */
public class BlogPostDeleted extends BlogPostEvent {

    /**
     * Instantiates a new {@code BlogPostDeleted} event.
     *
     * @param aggregateId the aggregate id
     */
    public BlogPostDeleted(UUID aggregateId) {
        super(aggregateId, ZonedDateTime.now());
    }

    /**
     * Returns aggregate id.
     *
     * @return the aggregate id
     */
    public UUID getAggregateId() {
        return super.aggregateId;
    }
}
