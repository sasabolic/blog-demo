package io.sixhours.blog.demo.common;

import org.apache.avro.generic.GenericRecord;

import java.time.Instant;
import java.time.ZonedDateTime;
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
    private final Instant dateCreated;

    /**
     * Instantiates a new {@code BlogPostCreated} event.
     *
     * @param aggregateId the aggregate id
     * @param title       the title
     * @param body        the body
     * @param author      the author
     * @param dateCreated the date created
     */
    public BlogPostCreated(UUID aggregateId, String title, String body, String author, Instant dateCreated) {
        super(aggregateId, ZonedDateTime.now());
        this.title = title;
        this.body = body;
        this.author = author;
        this.dateCreated = dateCreated;
    }

    public BlogPostCreated(GenericRecord record) {
        super(UUID.fromString(String.valueOf(record.get("aggregate_id"))), ZonedDateTime.now());
        this.title = String.valueOf(record.get("title"));
        this.body = String.valueOf(record.get("body"));
        this.author = String.valueOf(record.get("author"));
        this.dateCreated = Instant.parse(String.valueOf(record.get("date_created")));
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
    public Instant getDateCreated() {
        return dateCreated;
    }
}
