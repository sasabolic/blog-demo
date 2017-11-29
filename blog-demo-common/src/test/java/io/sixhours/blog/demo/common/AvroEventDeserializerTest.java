package io.sixhours.blog.demo.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.bijection.Injection;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import scala.util.Try;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AvroEventDeserializerTest {

    @InjectMocks
    AvroEventDeserializer deserializer;

    @Mock
    Injection<GenericRecord, byte[]> recordInjection;

    @Mock
    ObjectMapper mapper;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    GenericData.Record record;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        Try mTry = mock(Try.class);
        record = mock(GenericData.Record.class);
        Event event = new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now());

        doReturn(mTry).when(recordInjection).invert(any(byte[].class));
        doReturn(record).when(mTry).get();
        doReturn(BlogPostCreated.class.getCanonicalName()).when(record).get("class");
        doReturn(event).when(mapper).readValue(any(String.class), any(Class.class));
    }

    @Test
    public void whenDeserializeThenReturnDeserializedData() {
        doReturn("{test: test}").when(record).get("payload");

        Event deserializedData = deserializer.deserialize("blog-demo.post", "test".getBytes());

        assertThat(deserializedData, is(notNullValue()));
    }

    @Test
    public void givenBadPayloadWhenDeserializeThenThrowRuntimeException() {
        exception.expect(RuntimeException.class);

        doThrow(IOException.class).when(record).get("payload");

        deserializer.deserialize("blog-demo.post", "test".getBytes());
    }

    @Test
    public void givenDataWithValueNullWhenDeserializeThenReturnNull() throws JsonProcessingException {
        Event deserializedData = deserializer.deserialize("blog-demo.post", null);

        assertThat(deserializedData, is(nullValue()));
    }

}
