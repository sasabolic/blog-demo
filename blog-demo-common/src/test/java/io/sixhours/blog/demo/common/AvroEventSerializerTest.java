package io.sixhours.blog.demo.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;

public class AvroEventSerializerTest {

    @InjectMocks
    private AvroEventSerializer serializer;

    @Spy
    ObjectMapper mapper;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenSerializeThenReturnSerializedData() {
        Event event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());
        byte[] serializedData = serializer.serialize("blog-demo.post", event);

        assertThat(serializedData, is(notNullValue()));
    }

    @Test
    public void givenExceptionThrownWhenSerializeThenThrowRuntimeException() throws JsonProcessingException {
        Event event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());

        exception.expect(RuntimeException.class);

        doThrow(IOException.class).when(mapper).writeValueAsString(anyString());

        serializer.serialize("blog-demo.post", event);
    }

    @Test
    public void givenObjectWithValueNullWhenSerializeThenReturnNull() throws JsonProcessingException {
        Event event = null;

        byte[] serializedData = serializer.serialize("blog-demo.post", event);

        assertThat(serializedData, is(nullValue()));
    }
}
