package io.sixhours.blog.demo.common;

import java.time.ZonedDateTime;
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
    protected final UUID aggregateId;
    /**
     * Time when event was created.
     */
    protected final ZonedDateTime date;

    // TODO: move this to separate type KafkaEvent extends Event
    /**
     * Topic name where events will be send.
     */
    protected final String topicName;

    /**
     * Instantiates a new {@code Event}.
     *
     * @param aggregateId the aggregate id
     * @param date        the date
     * @param topicName   the topic name
     */
    public Event(UUID aggregateId, ZonedDateTime date, String topicName) {
        this.aggregateId = aggregateId;
        this.date = date;
        this.topicName = topicName;
    }
}
