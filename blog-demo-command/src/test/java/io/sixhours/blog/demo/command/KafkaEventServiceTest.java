package io.sixhours.blog.demo.command;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

public class KafkaEventServiceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private KafkaEventService eventService;
    private Producer<String, String> producer;
    private Future<RecordMetadata> savedEvent;

    @Before
    public void setUp() {
        eventService = Mockito.spy(new KafkaEventService());
        producer = mock(Producer.class);
        RecordMetadata metadata = new RecordMetadata(new TopicPartition("post", 1), 1, 1, 1, Long.valueOf(1), 1, 1);
        savedEvent = new Future<RecordMetadata>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public RecordMetadata get() throws InterruptedException, ExecutionException {
                return metadata;
            }

            @Override
            public RecordMetadata get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
    }

    @Test
    public void whenEventSentThenInvokeProducerSend() {
        doReturn(producer).when(eventService).getProducer();
        doReturn(savedEvent).when(producer).send(any(ProducerRecord.class));

        eventService.sendEvent(new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now()));

        verify(producer).send(any(ProducerRecord.class));
    }

    @Test
    public void givenExceptionWhenProducerSendThenThrowRuntimeException() {
        exception.expect(RuntimeException.class);

        doReturn(producer).when(eventService).getProducer();
        doThrow(new InterruptedException()).when(producer).send(any(ProducerRecord.class));

        eventService.sendEvent(new BlogPostCreated(UUID.randomUUID(), "Title", "Body", "Author", Instant.now()));
    }
}
