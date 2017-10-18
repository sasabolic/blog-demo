package io.sixhours.blog.demo.blogdemo;

import java.util.Date;
import java.util.UUID;

public class BlogPost {
    private UUID id;
    private String title;
    private String body;
    private String author;
    private Date dateCreated;

    private final EventService eventService;

    public BlogPost(EventService eventService) {
        this.eventService = eventService;
    }

    public void process(CreateBlogPostCommand command) {
        // verify

        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), command.getTitle(), command.getBody(), command.getAuthor(), new Date());
        this.eventService.sendEvent(EventService.TopicType.POST, event.getId().toString(), event.toString());

        apply(event);
    }

    public void apply(BlogPostCreated event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.body = event.getBody();
        this.author = event.getAuthor();
        this.dateCreated = event.getDateCreated();
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
