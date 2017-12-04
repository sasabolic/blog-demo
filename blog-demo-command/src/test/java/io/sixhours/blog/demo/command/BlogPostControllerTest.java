package io.sixhours.blog.demo.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BlogPostController.class, secure = false)
public class BlogPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @Test
    public void GivenValidRequestWhenCreatePostThenReturnStatusCreated() throws Exception {
        final UUID uuid = UUID.randomUUID();
        doReturn(uuid).when(blogService).create(anyString(), anyString(), anyString());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/blog_posts")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"title\": \"Na Drini Cuprija\",\n" +
                        "  \"body\": \"Mnogo teksta\",\n" +
                        "  \"author\": \"Ivo Andric\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        final MockHttpServletResponse response = result.getResponse();

        assertThat(response.getStatus(), is(equalTo(HttpStatus.CREATED.value())));
    }

    @Test
    public void GivenValidRequestWhenCreatePostThenReturnHeaderLocation() throws Exception {
        final UUID uuid = UUID.randomUUID();
        doReturn(uuid).when(blogService).create(anyString(), anyString(), anyString());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/blog_posts")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"title\": \"Na Drini Cuprija\",\n" +
                        "  \"body\": \"Mnogo teksta\",\n" +
                        "  \"author\": \"Ivo Andric\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        final MockHttpServletResponse response = result.getResponse();

        assertThat(response.getHeader(HttpHeaders.LOCATION), containsString(uuid.toString()));
    }
}
