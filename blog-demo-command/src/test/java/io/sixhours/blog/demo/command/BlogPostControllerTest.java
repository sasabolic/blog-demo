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
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BlogPostController.class, secure = false)
public class BlogPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @Test
    public void givenValidRequestWhenCreatePostThenReturnStatusCreated() throws Exception {
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
    public void givenValidRequestWhenCreatePostThenReturnHeaderLocation() throws Exception {
        final UUID uuid = UUID.randomUUID();
        doReturn(uuid).when(blogService).create(anyString(), anyString(), anyString());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/blog_posts")
                .content("{\n" +
                        "  \"title\": \"Na Drini Ćuprija\",\n" +
                        "  \"body\": \"Mnogo teksta\",\n" +
                        "  \"author\": \"Ivo Andrić\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        final MockHttpServletResponse response = result.getResponse();

        assertThat(response.getHeader(HttpHeaders.LOCATION), containsString(uuid.toString()));
    }

    @Test
    public void givenValidRequestWhenCreatePostThenInvokeBlogServiceCreate() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/blog_posts")
                .content("{\n" +
                        "  \"title\": \"Na Drini Ćuprija\",\n" +
                        "  \"body\": \"Tekst\",\n" +
                        "  \"author\": \"Ivo Andrić\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        verify(blogService).create("Na Drini Ćuprija", "Tekst", "Ivo Andrić");
    }

    @Test
    public void givenValidRequestWhenUpdatePostThenReturnStatusOk() throws Exception {
        final UUID uuid = UUID.randomUUID();
        doReturn(uuid).when(blogService).create(anyString(), anyString(), anyString());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/blog_posts/{aggregateId}", uuid)
                .content("{\n" +
                        "  \"title\": \"Na Drini Cuprija\",\n" +
                        "  \"body\": \"Content\",\n" +
                        "  \"author1\": \"Ivo Andric\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();

        assertThat(response.getStatus(), is(equalTo(HttpStatus.OK.value())));
    }

    @Test
    public void givenValidRequestWhenUpdatePostThenInvokeBlogServiceUpdate() throws Exception {
        final UUID uuid = UUID.randomUUID();

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/blog_posts/{aggregateId}", uuid)
                .content("{\n" +
                        "  \"title\": \"Na Drini Cuprija\",\n" +
                        "  \"body\": \"Content\",\n" +
                        "  \"author\": \"Ivo Andric\"\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        verify(blogService).update(uuid, "Na Drini Cuprija", "Content", "Ivo Andric");
    }

    @Test
    public void givenValidRequestWhenDeletePostThenReturnStatusAccepted() throws Exception {
        final UUID uuid = UUID.randomUUID();

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/blog_posts/{aggregateId}", uuid);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();

        assertThat(response.getStatus(), is(equalTo(HttpStatus.ACCEPTED.value())));
    }

    @Test
    public void givenValidRequestWhenDeletePostThenInvokeBlogServiceDelete() throws Exception {
        final UUID uuid = UUID.randomUUID();

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/blog_posts/{aggregateId}", uuid);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        final MockHttpServletResponse response = result.getResponse();

        verify(blogService).delete(uuid);
    }
}
