package io.sixhours.blog.demo.query;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KafkaConsumerServiceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    @Spy
    private KafkaConsumerService consumerService = new KafkaConsumerService("kafka-consumer-test", Collections.singletonList("blog-demo.post"), false);

    @Mock
    private Consumer<String, byte[]> consumer;

    private ConsumerRecords<String, byte[]> records = mock(ConsumerRecords.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenConsumerServiceRunThenInvokeConsumerPoll() {
        doReturn(records).when(consumer).poll(anyLong());

        consumerService.run();

        verify(consumer).poll(anyLong());
    }

    @Test
    public void whenConsumerServiceShutdownThenInvokeConsumerWakeup() {
        consumerService.shutdown();

        verify(consumer).wakeup();
    }
}
