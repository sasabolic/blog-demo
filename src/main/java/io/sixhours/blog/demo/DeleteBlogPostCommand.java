package io.sixhours.blog.demo;

import java.util.UUID;

public class DeleteBlogPostCommand {

    private final UUID aggregateId;

    public DeleteBlogPostCommand(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }
}
