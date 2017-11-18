package io.sixhours.blog.demo.common;

import java.time.Instant;
import java.util.UUID;

/**
 * Event send by system.
 *
 * @author Sasa Bolic
 */
public abstract class Event {

    /**
     * ID of the aggregate on which event occured.
     */
    protected UUID aggregateId;
    /**
     * Time when event was created.
     */
    protected final Instant date;

    // TODO: move this to separate type KafkaEvent extends Event
    /**
     * Topic name where events will be send.
     */
    protected String topicName;

    /**
     * Instantiates a new {@code Event}.
     *
     * @param aggregateId the aggregate id
     * @param date        the date
     * @param topicName   the topic name
     */
    public Event(UUID aggregateId, Instant date, String topicName) {
        this.aggregateId = aggregateId;
        this.date = date;
        this.topicName = topicName;
    }

    public Event() {
        this.date = Instant.now();
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public Instant getDate() {
        return date;
    }

    public String getTopicName() {
        return topicName;
    }
}
