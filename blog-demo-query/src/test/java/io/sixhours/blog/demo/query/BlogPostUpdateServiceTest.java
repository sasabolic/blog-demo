package io.sixhours.blog.demo.query;

import io.sixhours.blog.demo.common.BlogPostCreated;
import io.sixhours.blog.demo.common.BlogPostDeleted;
import io.sixhours.blog.demo.common.BlogPostUpdated;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

public class BlogPostUpdateServiceTest {

    BlogPostUpdateService updateService = new BlogPostUpdateService();

    @Test
    public void whenCreateThenInvokeClientIndex() {
        BlogPostCreated blogPostCreated = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());

        updateService.create(blogPostCreated);

    }

    @Test
    public void whenUpdateThenInvokeClientIndex() {
        BlogPostCreated blogPostCreated = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());
        BlogPostUpdated blogPostUpdated = new BlogPostUpdated(blogPostCreated.getAggregateId(), "TitleUpdated", "BodyUpdated", "AuthorUpdated");

        updateService.create(blogPostCreated);

        updateService.update(blogPostUpdated);

    }

    @Test
    public void whenDeleteThenInvokeClientDelete() {
        BlogPostCreated blogPostCreated = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());
        BlogPostDeleted blogPostDeleted = new BlogPostDeleted(blogPostCreated.getAggregateId());

        updateService.create(blogPostCreated);

        updateService.delete(blogPostDeleted);

    }
}
