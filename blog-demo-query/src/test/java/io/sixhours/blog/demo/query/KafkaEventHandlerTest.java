package io.sixhours.blog.demo.query;

import io.sixhours.blog.demo.common.BlogPostCreated;
import io.sixhours.blog.demo.common.Event;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class KafkaEventHandlerTest {

    @Mock
    private AvroService avroService;

    @Mock
    private EventHandler eventHandler;

    @InjectMocks
    @Spy
    private KafkaEventHandler handler;

    private ConsumerRecord<String, byte[]> consumerRecord;
    private Header schemaHeader;
    private Header classHeader;
    private GenericRecord genericRecord;
    private Schema schema;

    @Before
    public void setUp() {
        consumerRecord = mock(ConsumerRecord.class);
        Headers headers = mock(Headers.class);
        schemaHeader = mock(Header.class);
        classHeader = mock(Header.class);
        genericRecord = mock(GenericRecord.class);
        schema = mock(Schema.class);

        doReturn(headers).when(consumerRecord).headers();
        doReturn(schemaHeader).when(headers).lastHeader("schema");
        doReturn(classHeader).when(headers).lastHeader("class");
        doReturn("dummy".getBytes()).when(consumerRecord).value();


        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenConsumerRecordOfCreateEventInvokeHandle() {
        doReturn("blog_post_created.avsc".getBytes()).when(schemaHeader).value();
        doReturn(BlogPostCreated.class.getCanonicalName().getBytes()).when(classHeader).value();
        doReturn(schema).when(avroService).getSchema(anyString());
        doReturn(genericRecord).when(avroService).decode(any(byte[].class), any(Schema.class));

        doReturn(UUID.randomUUID().toString()).when(genericRecord).get("aggregate_id");
        doReturn("title").when(genericRecord).get("title");
        doReturn("body").when(genericRecord).get("body");
        doReturn("author").when(genericRecord).get("author");
        doReturn(Instant.now().toString()).when(genericRecord).get("date_created");

        handler.handle(consumerRecord);

        verify(eventHandler).handle(any(Event.class));
    }
}
