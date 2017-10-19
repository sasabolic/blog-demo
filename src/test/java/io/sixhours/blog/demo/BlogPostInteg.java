package io.sixhours.blog.demo;

import org.junit.Before;
import org.junit.Test;

public class BlogPostInteg {

    private BlogPost blogPost;

    @Before
    public void setUp() {
        blogPost = new BlogPost(new KafkaEventService());
    }

    @Test
    public void test() {
        blogPost.process(new CreateBlogPostCommand("Title", "Body", "Author"));
    }
}
