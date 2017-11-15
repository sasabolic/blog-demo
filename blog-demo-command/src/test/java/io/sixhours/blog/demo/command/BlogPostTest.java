package io.sixhours.blog.demo.command;

import io.sixhours.blog.demo.common.BlogPostCreated;
import io.sixhours.blog.demo.common.BlogPostDeleted;
import io.sixhours.blog.demo.common.BlogPostUpdated;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BlogPostTest {

    private BlogPost blogPost;
    private EventService eventService;

    @Before
    public void setUp() {
        eventService = mock(EventService.class);
        blogPost = Mockito.spy(new BlogPost(eventService));
    }

    @Test
    public void givenCreateBlogPostCommandWhenProcessThenInovkeEventServiceSendEvent() {
        CreateBlogPostCommand command = new CreateBlogPostCommand("Title", "Body", "Author");

        blogPost.process(command);

        verify(eventService).sendEvent(isA(BlogPostCreated.class));
    }

    @Test
    public void givenCreateBlogPostCommandWhenProcessThenInvokeApply() {
        CreateBlogPostCommand command = new CreateBlogPostCommand("Title", "Body", "Author");

        blogPost.process(command);

        verify(blogPost).apply(isA(BlogPostCreated.class));
    }

    @Test
    public void givenBlogPostCreatedWhenApplyThenIdIsStored() {
        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("id", equalTo(event.getAggregateId())));
    }

    @Test
    public void givenBlogPostCreatedWhenApplyThenTitleIsStored() {
        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("deleted", equalTo(false)));
    }

    @Test
    public void givenBlogPostCreatedWhenApplyThenBodyIsStored() {
        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("body", equalTo(event.getBody())));
    }

    @Test
    public void givenBlogPostCreatedWhenApplyThenCreatedDateIsStored() {
        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("dateCreated", equalTo(event.getDateCreated())));
    }

    @Test
    public void givenBlogPostCreatedWhenApplyThenDeleteIsFalse() {
        BlogPostCreated event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("dateCreated", equalTo(event.getDateCreated())));
    }

    @Test
    public void givenDeleteBlogPostCommandWhenProcessThenInvokeApply() {
        DeleteBlogPostCommand command = new DeleteBlogPostCommand(UUID.randomUUID());

        blogPost.process(command);

        verify(blogPost).apply(isA(BlogPostDeleted.class));
    }

    @Test
    public void givenDeleteBlogPostCommandWhenProcessThenInovkeEventServiceSendEvent() {
        DeleteBlogPostCommand command = new DeleteBlogPostCommand(UUID.randomUUID());

        blogPost.process(command);

        verify(eventService).sendEvent(isA(BlogPostDeleted.class));
    }

    @Test
    public void givenBlogPostDeletedWhenApplyThenDeleteIsTrue() {
        BlogPostDeleted event = new BlogPostDeleted(UUID.randomUUID());

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("deleted", equalTo(true)));
    }

    @Test
    public void givenUpdateBlogPostCommandWhenProcessThenInvokeApply() {
        UpdateBlogPostCommand command = new UpdateBlogPostCommand(UUID.randomUUID(), "Title1", "Body1", "Author1");

        blogPost.process(command);

        verify(blogPost).apply(isA(BlogPostUpdated.class));
    }

    @Test
    public void givenUpdateBlogPostCommandWhenProcessThenInovkeEventServiceSendEvent() {
        UpdateBlogPostCommand command = new UpdateBlogPostCommand(UUID.randomUUID(), "Title1", "Body1", "Author1");

        blogPost.process(command);

        verify(eventService).sendEvent(isA(BlogPostUpdated.class));
    }

    @Test
    public void givenBlogPostUpdatedWhenApplyThenIdIsStored() {
        BlogPostUpdated event = new BlogPostUpdated(UUID.randomUUID(), "Title1", "Body1", "Author1");

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("id", equalTo(event.getAggregateId())));
    }

    @Test
    public void givenBlogPostUpdatedWhenApplyThenTitleIsStored() {
        BlogPostUpdated event = new BlogPostUpdated(UUID.randomUUID(), "Title1", "Body1", "Author1");

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("title", equalTo(event.getTitle())));
    }

    @Test
    public void givenBlogPostUpdatedWhenApplyThenBodyIsStored() {
        BlogPostUpdated event = new BlogPostUpdated(UUID.randomUUID(), "Title1", "Body1", "Author1");

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("body", equalTo(event.getBody())));
    }

    @Test
    public void givenBlogPostUpdatedWhenApplyThenAuthorIsStored() {
        BlogPostUpdated event = new BlogPostUpdated(UUID.randomUUID(), "Title1", "Body1", "Author1");

        blogPost.apply(event);

        assertThat(blogPost, hasProperty("author", equalTo(event.getAuthor())));
    }
}
