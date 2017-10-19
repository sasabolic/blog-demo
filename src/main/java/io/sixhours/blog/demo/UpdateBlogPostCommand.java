package io.sixhours.blog.demo;

import java.util.UUID;

public class UpdateBlogPostCommand {

    private final UUID aggregateId;
    private final String title;
    private final String body;
    private final String author;

    public UpdateBlogPostCommand(UUID aggregateId, String title, String body, String author) {
        this.aggregateId = aggregateId;
        this.title = title;
        this.body = body;
        this.author = author;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }
}
