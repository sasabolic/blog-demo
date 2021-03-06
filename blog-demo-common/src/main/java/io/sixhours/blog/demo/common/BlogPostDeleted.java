package io.sixhours.blog.demo.common;

import org.apache.avro.generic.GenericRecord;

import java.time.Instant;
import java.util.UUID;

/**
 * Event triggered when blog post is deleted.
 *
 * @author Sasa Bolic
 */
public class BlogPostDeleted extends BlogPostEvent {

    public BlogPostDeleted() {
        super();
    }
    /**
     * Instantiates a new {@code BlogPostDeleted} event.
     *
     * @param aggregateId the aggregate id
     */
    public BlogPostDeleted(UUID aggregateId) {
        super(aggregateId, Instant.now());
    }

    public BlogPostDeleted(GenericRecord record) {
        super(UUID.fromString(String.valueOf(record.get("aggregate_id"))), Instant.now());
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
