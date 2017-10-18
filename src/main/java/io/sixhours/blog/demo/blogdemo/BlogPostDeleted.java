package io.sixhours.blog.demo.blogdemo;

import java.time.ZonedDateTime;
import java.util.UUID;

public class BlogPostDeleted extends BlogPostEvent {

    public BlogPostDeleted(UUID aggregateId) {
        super(aggregateId, ZonedDateTime.now());
    }

    public UUID getAggregateId() {
        return super.aggregateId;
    }
}
