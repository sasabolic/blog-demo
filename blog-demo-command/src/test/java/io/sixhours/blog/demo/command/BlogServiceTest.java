package io.sixhours.blog.demo.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class BlogServiceTest {


    @Mock
    private EventService eventService;

    @Mock
    private BlogPost blogPost;

    @InjectMocks
    private BlogService blogService = new BlogServiceImpl(eventService);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenBlogServiceCreateThenReturnUUID() {
        doReturn(UUID.randomUUID()).when(blogPost).getId();

        final UUID aggregateId = blogService.create("Na Drini Cuprija", "Tekst", "Ivo Andric");

        assertThat(aggregateId, notNullValue());
    }

    @Test
    public void whenBlogServiceCreateThenInvokeBlogPostProcessCreateBlogPostCommand() {
        final UUID aggregateId = blogService.create("Na Drini Cuprija", "Tekst", "Ivo Andric");

        verify(blogPost).process(isA(CreateBlogPostCommand.class));
    }

    @Test
    public void whenBlogServiceUpdateThenInvokeBlogPostProcessUpdateBlogPostCommand() {
        blogService.update(UUID.randomUUID(), "Na Drini Cuprija", "Tekst", "Ivo Andric");

        verify(blogPost).process(isA(UpdateBlogPostCommand.class));
    }

    @Test
    public void whenBlogServiceDeleteThenInvokeBlogPostProcessDeleteBlogPostCommand() {
        blogService.delete(UUID.randomUUID());

        verify(blogPost).process(isA(DeleteBlogPostCommand.class));
    }
}
