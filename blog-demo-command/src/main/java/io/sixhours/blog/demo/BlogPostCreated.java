package io.sixhours.blog.demo;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * Event triggered when blog post is created.
 *
 * @author Sasa Bolic
 */
public class BlogPostCreated extends BlogPostEvent {

    private final String title;
    private final String body;
    private final String author;
    private final Date dateCreated;

    /**
     * Instantiates a new {@code BlogPostCreated} event.
     *
     * @param aggregateId the aggregate id
     * @param title       the title
     * @param body        the body
     * @param author      the author
     * @param dateCreated the date created
     */
    public BlogPostCreated(UUID aggregateId, String title, String body, String author, Date dateCreated) {
        super(aggregateId, ZonedDateTime.now());
        this.title = title;
        this.body = body;
        this.author = author;
        this.dateCreated = dateCreated;
    }

    /**
     * Returns aggregate id.
     *
     * @return the aggregate id
     */
    public UUID getAggregateId() {
        return super.aggregateId;
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

    /**
     * Returns date created.
     *
     * @return the date created
     */
    public Date getDateCreated() {
        return dateCreated;
    }
}
