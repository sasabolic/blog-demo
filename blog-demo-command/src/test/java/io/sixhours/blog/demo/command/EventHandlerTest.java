package io.sixhours.blog.demo.command;

import io.sixhours.blog.demo.common.BlogPostCreated;
import io.sixhours.blog.demo.common.BlogPostDeleted;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EventHandlerTest {

    private BlogPostCreatedHandler blogPostCreatedHandler;
    private BlogPostDeletedHandler blogPostDeletedHandler;
    private BlogPostUpdatedHandler blogPostUpdatedHandler;
    private EventHandler eventHandler;

    @Before
    public void setUp() {
        blogPostDeletedHandler = Mockito.spy(BlogPostDeletedHandler.class);
        blogPostCreatedHandler = Mockito.spy(BlogPostCreatedHandler.class);
        blogPostUpdatedHandler = Mockito.spy(BlogPostUpdatedHandler.class);

        blogPostDeletedHandler.setNext(blogPostCreatedHandler);
        blogPostCreatedHandler.setNext(blogPostUpdatedHandler);

        eventHandler = blogPostDeletedHandler;
    }

    @Test
    public void whenBlogPostCreatedThenInvokeBlogPostCreatedHandler() {
        final BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());

        eventHandler.encode(event);

        verify(blogPostCreatedHandler, times(1)).encode(event);
    }

    @Test
    public void whenBlogPostDeletedThenNotInvokeBlogPostCreatedHandler() {
        final BlogPostDeleted event = new BlogPostDeleted(UUID.randomUUID());

        eventHandler.encode(event);

        verify(blogPostCreatedHandler, times(0)).encode(event);
    }

    @Test
    public void whenBlogPostCreatedThenNotInvokeBlogPostUpdatedHandler() {
        final BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());

        eventHandler.encode(event);

        verify(blogPostCreatedHandler, times(1)).encode(event);
    }
}
