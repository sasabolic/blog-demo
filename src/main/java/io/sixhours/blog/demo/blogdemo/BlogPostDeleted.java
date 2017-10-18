package io.sixhours.blog.demo.blogdemo;

import java.util.UUID;

public class BlogPostDeleted implements BlogPostEvent {

    private final UUID aggregateId;

    public BlogPostDeleted(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }
}
