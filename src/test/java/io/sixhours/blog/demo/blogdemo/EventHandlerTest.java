package io.sixhours.blog.demo.blogdemo;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class EventHandlerTest {

    private BlogPostCreatedHandler blogPostCreatedHandler;
    private BlogPostDeletedHandler blogPostDeletedHandler;
    private EventHandler eventHandler;

    @Before
    public void setUp() {
        blogPostDeletedHandler = spy(BlogPostDeletedHandler.class);
        blogPostCreatedHandler = spy(BlogPostCreatedHandler.class);

        blogPostDeletedHandler.setNext(blogPostCreatedHandler);

        eventHandler = blogPostDeletedHandler;
    }

    @Test
    public void whenBlogPostCreatedThenInvokeBlogPostCreatedHandler() {
        final BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", new Date());

        eventHandler.handleRequest(event);

        verify(blogPostCreatedHandler, times(1)).handleRequest(event);
    }

    @Test
    public void whenBlogPostDeletedThenNotInvokeBlogPostCreatedHandler() {
        final BlogPostDeleted event = new BlogPostDeleted(UUID.randomUUID());

        eventHandler.handleRequest(event);

        verify(blogPostCreatedHandler, times(0)).handleRequest(event);
    }
}
