package io.sixhours.blog.demo.command;

import java.util.Date;
import java.util.UUID;

/**
 * Aggregate representing blog post.
 *
 * @author Sasa Bolic
 */
public class BlogPost {
    private UUID id;
    private String title;
    private String body;
    private String author;
    private Date dateCreated;
    private boolean deleted;

    private final EventService eventService;

    /**
     * Instantiates a new {@code BlogPost}.
     *
     * @param eventService the event service
     */
    public BlogPost(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Processes {@link CreateBlogPostCommand}.
     *
     * @param command the command
     */
    public void process(CreateBlogPostCommand command) {
        // TODO: verify

        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), command.getTitle(), command.getBody(), command.getAuthor(), new Date());
        this.eventService.sendEvent(event);

        apply(event);
    }

    /**
     * Processes {@link  DeleteBlogPostCommand}.
     *
     * @param command the command
     */
    public void process(DeleteBlogPostCommand command) {

        BlogPostDeleted event = new BlogPostDeleted(command.getAggregateId());
        apply(event);
        this.eventService.sendEvent(event);
    }

    /**
     * Processes {@link UpdateBlogPostCommand}.
     *
     * @param command the command
     */
    public void process(UpdateBlogPostCommand command) {

        BlogPostUpdated event = new BlogPostUpdated(command.getAggregateId(), command.getTitle(), command.getBody(), command.getAuthor());
        this.eventService.sendEvent(event);
        apply(event);
    }

    /**
     * Executes changes on aggregate invoked by {@link BlogPostCreated} event.
     *
     * @param event the event
     */
    public void apply(BlogPostCreated event) {
        this.id = event.getAggregateId();
        this.title = event.getTitle();
        this.body = event.getBody();
        this.author = event.getAuthor();
        this.dateCreated = event.getDateCreated();
    }

    /**
     * Executes changes on aggregate invoked by {@link BlogPostDeleted} event.
     *
     * @param event the event
     */
    public void apply(BlogPostDeleted event) {
        this.deleted = true;
    }

    /**
     * Executes changes on aggregate invoked by {@link BlogPostUpdated} event.
     *
     * @param event the event
     */
    public void apply(BlogPostUpdated event) {
        this.id = event.getAggregateId();
        this.title = event.getTitle();
        this.body = event.getBody();
        this.author = event.getAuthor();
    }

    /**
     * Returns id of blog post;
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns title of blog post.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns body of blog post.
     *
     * @return the body
     */
    public String getBody() {
        return body;
    }

    // TODO: Author name value object?
    /**
     * Returns author name of blog post.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns date when blog post was created.
     *
     * @return the date created
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Checks if  blog post is in deleted state.
     *
     * @return the boolean
     */
    public boolean isDeleted() {
        return deleted;
    }
}
