package io.sixhours.blog.demo.command;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class BlogPostInteg {

    private BlogPost blogPost;

    @Before
    public void setUp() {
        blogPost = new BlogPost(new KafkaEventService());
    }

    @Test
    public void test() {

        blogPost.process(new CreateBlogPostCommand("Title", "Body", "Author"));
        UUID firstBlogPostId = blogPost.getId();

        blogPost.process(new CreateBlogPostCommand("Title1", "Body1", "Author1"));
        blogPost.process(new CreateBlogPostCommand("Title2", "Body2", "Author2"));
        blogPost.process(new CreateBlogPostCommand("Title3", "Body3", "Author3"));
        blogPost.process(new CreateBlogPostCommand("Title4", "Body4", "Author4"));
        blogPost.process(new CreateBlogPostCommand("Title5", "Body5", "Author5"));
        blogPost.process(new CreateBlogPostCommand("Title6", "Body6", "Author6"));
        blogPost.process(new CreateBlogPostCommand("Title7", "Body7", "Author7"));
        UUID lastBlogPostId = blogPost.getId();

        blogPost.process(new UpdateBlogPostCommand(firstBlogPostId, "UpdateTitle", "UpdateTitle", "UpdateAuthor"));
        // update last
        blogPost.process(new UpdateBlogPostCommand(lastBlogPostId, "UpdateTitle", "UpdateTitle", "UpdateAuthor"));

        // delete last
        blogPost.process(new DeleteBlogPostCommand(lastBlogPostId));

    }

}
