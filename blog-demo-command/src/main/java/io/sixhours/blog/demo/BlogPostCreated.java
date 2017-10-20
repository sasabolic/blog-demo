package io.sixhours.blog.demo;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

public class BlogPostCreated extends BlogPostEvent {

    private final String title;
    private final String body;
    private final String author;
    private final Date dateCreated;

    public BlogPostCreated(UUID aggregateId, String title, String body, String author, Date dateCreated) {
        super(aggregateId, ZonedDateTime.now());
        this.title = title;
        this.body = body;
        this.author = author;
        this.dateCreated = dateCreated;
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

    public Date getDateCreated() {
        return dateCreated;
    }
}
