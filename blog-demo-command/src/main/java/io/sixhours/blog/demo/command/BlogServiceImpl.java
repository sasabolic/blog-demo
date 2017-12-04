package io.sixhours.blog.demo.command;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of {@link BlogService}.
 *
 * @author Sasa Bolic
 */
@Service
public class BlogServiceImpl implements BlogService {

    private BlogPost blogPost;

    /**
     * Instantiates a new {@code BlogServiceImpl}.
     */
    public BlogServiceImpl(EventService eventService) {
        blogPost = new BlogPost(eventService);
    }

    @Override
    public UUID create(String title, String body, String author) {
        blogPost.process(new CreateBlogPostCommand(title, body, author));

        return blogPost.getId();
    }

    @Override
    public void update(UUID aggregateId, String title, String body, String author) {
        blogPost.process(new UpdateBlogPostCommand(aggregateId, title, body, author));
    }

    @Override
    public void delete(UUID aggregateId) {
        blogPost.process(new DeleteBlogPostCommand(aggregateId));
    }
}
