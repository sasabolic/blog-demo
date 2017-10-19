package io.sixhours.blog.demo;

import java.util.Date;
import java.util.UUID;

public class BlogPost {
    private UUID id;
    private String title;
    private String body;
    private String author;
    private Date dateCreated;
    private boolean deleted;

    private final EventService eventService;

    public BlogPost(EventService eventService) {
        this.eventService = eventService;
    }

    public void process(CreateBlogPostCommand command) {
        // verify

        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), command.getTitle(), command.getBody(), command.getAuthor(), new Date());
        this.eventService.sendEvent(event);

        apply(event);
    }

    public void process(DeleteBlogPostCommand command) {

        BlogPostDeleted event = new BlogPostDeleted(command.getAggregateId());
        apply(event);
        this.eventService.sendEvent(event);
    }

    public void process(UpdateBlogPostCommand command) {

        BlogPostUpdated event = new BlogPostUpdated(command.getAggregateId(), command.getTitle(), command.getBody(), command.getAuthor());
        this.eventService.sendEvent(event);
        apply(event);
    }

    public void apply(BlogPostCreated event) {
        this.id = event.getAggregateId();
        this.title = event.getTitle();
        this.body = event.getBody();
        this.author = event.getAuthor();
        this.dateCreated = event.getDateCreated();
    }

    public void apply(BlogPostDeleted event) {
        this.deleted = true;
    }

    public void apply(BlogPostUpdated event) {
        this.id = event.getAggregateId();
        this.title = event.getTitle();
        this.body = event.getBody();
        this.author = event.getAuthor();
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

    public boolean isDeleted() {
        return deleted;
    }
}
