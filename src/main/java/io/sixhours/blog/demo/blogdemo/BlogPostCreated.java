package io.sixhours.blog.demo.blogdemo;

import java.util.Date;
import java.util.UUID;

public class BlogPostCreated implements Event {

    private final UUID aggregateId;
    private final String title;
    private final String body;
    private final String author;
    private final Date dateCreated;

    public BlogPostCreated(UUID aggregateId, String title, String body, String author, Date dateCreated) {
        this.aggregateId = aggregateId;
        this.title = title;
        this.body = body;
        this.author = author;
        this.dateCreated = dateCreated;
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

    public Date getDateCreated() {
        return dateCreated;
    }
}
