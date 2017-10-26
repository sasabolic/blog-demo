package io.sixhours.blog.demo;

import java.util.UUID;

/**
 * Command used to delete blog post.
 *
 * @author Sasa Bolic
 */
public class DeleteBlogPostCommand {

    private final UUID aggregateId;

    /**
     * Instantiates a new {@code DeleteBlogPostCommand}.
     *
     * @param aggregateId the aggregate id
     */
    public DeleteBlogPostCommand(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    /**
     * Returns aggregate id.
     *
     * @return the aggregate id
     */
    public UUID getAggregateId() {
        return aggregateId;
    }
}
