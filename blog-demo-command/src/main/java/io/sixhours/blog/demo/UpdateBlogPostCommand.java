package io.sixhours.blog.demo;

import java.util.UUID;

/**
 * Command used to update blog post.
 *
 * @author Sasa Bolic
 */
public class UpdateBlogPostCommand {

    private final UUID aggregateId;
    private final String title;
    private final String body;
    private final String author;

    /**
     * Instantiates a new {@code UpdateBlogPostCommand}.
     *
     * @param aggregateId the aggregate id
     * @param title       the title
     * @param body        the body
     * @param author      the author
     */
    public UpdateBlogPostCommand(UUID aggregateId, String title, String body, String author) {
        this.aggregateId = aggregateId;
        this.title = title;
        this.body = body;
        this.author = author;
    }

    /**
     * Returns aggregate id.
     *
     * @return the aggregate id
     */
    public UUID getAggregateId() {
        return aggregateId;
    }

    /**
     * Returns title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns body.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }
}
