package io.sixhours.blog.demo.blogdemo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class BlogPostTest {

    private BlogPost blogPost;
    private EventService eventService;

    @Before
    public void setUp() {
        eventService = mock(EventService.class);
        blogPost = spy(new BlogPost(eventService));
    }

    @Test
    public void whenCreateBlogPostThenBlogPostEventCreatedIsPublished() {
        CreateBlogPostCommand command = new CreateBlogPostCommand("Title", "Body", "Author");

        blogPost.process(command);

        final ArgumentCaptor<EventService.TopicType> topicTypeArgumentCaptor = ArgumentCaptor.forClass(EventService.TopicType.class);

        verify(eventService).sendEvent(topicTypeArgumentCaptor.capture(), isA(String.class), isA(Event.class));

        assertThat(topicTypeArgumentCaptor.getValue(), is(EventService.TopicType.POST));
    }

    @Test
    public void whenCreateBlogPostThenInvokeApply() {
        CreateBlogPostCommand command = new CreateBlogPostCommand("Title", "Body", "Author");

        blogPost.process(command);

        verify(blogPost).apply(isA(BlogPostCreated.class));
    }

    @Test
    public void whenBlogPostCreatedThenIdIsStored() {
        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", new Date());

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("id", equalTo(event.getAggregateId())));
    }

    @Test
    public void whenBlogPostCreatedThenTitleIsStored() {
        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", new Date());

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("title", equalTo(event.getTitle())));
    }

    @Test
    public void whenBlogPostCreatedThenBodyIsStored() {
        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", new Date());

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("body", equalTo(event.getBody())));
    }

    @Test
    public void whenBlogPostCreatedThenCreatedDateIsStored() {
        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", new Date());

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("dateCreated", equalTo(event.getDateCreated())));
    }

    @Test
    public void whenDeleteBlogPostThenInvokeApply() {
        DeleteBlogPostCommand command = new DeleteBlogPostCommand(UUID.randomUUID());

        blogPost.process(command);

        verify(blogPost).apply(isA(BlogPostDeleted.class));
    }
}
