package io.sixhours.blog.demo;

import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class BlogPostEvent extends Event {

    public BlogPostEvent(UUID aggregateId, ZonedDateTime date) {
        super(aggregateId, date, "post");
    }
}
