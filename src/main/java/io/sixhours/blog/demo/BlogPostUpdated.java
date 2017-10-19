package io.sixhours.blog.demo;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

public class BlogPostUpdated extends BlogPostEvent {

    private final String title;
    private final String body;
    private final String author;

    public BlogPostUpdated(UUID aggregateId, String title, String body, String author) {
        super(aggregateId, ZonedDateTime.now());
        this.title = title;
        this.body = body;
        this.author = author;
    }

    public UUID getAggregateId() {
        return super.aggregateId;
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
