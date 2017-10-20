package io.sixhours.blog.demo;

import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class Event {

    protected final UUID aggregateId;
    protected final ZonedDateTime date;
    protected final String topicName;

    public Event(UUID aggregateId, ZonedDateTime date, String topicName) {
        this.aggregateId = aggregateId;
        this.date = date;
        this.topicName = topicName;
    }
}
