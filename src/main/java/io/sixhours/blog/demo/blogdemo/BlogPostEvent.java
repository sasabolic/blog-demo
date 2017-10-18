package io.sixhours.blog.demo.blogdemo;

import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class BlogPostEvent extends Event {

    protected final String topicName = "post";

    public BlogPostEvent(UUID aggregateId, ZonedDateTime date) {
        super(aggregateId, date);
    }
}
