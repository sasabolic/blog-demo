package io.sixhours.blog.demo.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;

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
        final BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", new Date());

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
        final BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", new Date());

        eventHandler.encode(event);

        verify(blogPostCreatedHandler, times(1)).encode(event);
    }
}
