package io.sixhours.blog.demo.blogdemo;

import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class Event {

    protected final UUID aggregateId;
    protected final ZonedDateTime date;

    public Event(UUID aggregateId, ZonedDateTime date) {
        this.aggregateId = aggregateId;
        this.date = date;
    }
}
