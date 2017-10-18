package io.sixhours.blog.demo.blogdemo;

import java.util.Date;
import java.util.UUID;

public class BlogPostCreated {

    private final UUID id;
    private final String title;
    private final String body;
    private final String author;
    private final Date dateCreated;

    public BlogPostCreated(UUID id, String title, String body, String author, Date dateCreated) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.dateCreated = dateCreated;
    }

    public UUID getId() {
        return id;
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
