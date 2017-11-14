package io.sixhours.blog.demo.command;

import com.twitter.bijection.Injection;
import net.bytebuddy.pool.TypePool;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
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
import scala.util.Try;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AvroServiceTest {

    @InjectMocks
    @Spy
    private AvroService avroService = new AvroService("schemas/blog_post_created.avsc");

    @Mock
    private Injection<GenericRecord, byte[]> recordInjection;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenEncodeThenInvokeRecordPut() {
        avroService.encode(Map.of("aggregate_id", "value"));

        verify(recordInjection).apply(isA(GenericRecord.class));
    }

    @Test
    public void whenDecodeThenInvokeRecordInjectionInver() {
        Try mTry = mock(Try.class);
        GenericData.Record record = mock(GenericData.Record.class);

        doReturn(mTry).when(recordInjection).invert(any(byte[].class));
        doReturn(record).when(mTry).get();

        avroService.decode("data".getBytes());

        verify(recordInjection).invert(isA(byte[].class));
    }

    @Test
    public void givenNonExistingSchemaLocationWhenInstantiatedThenThrowRuntimeException() {
        exception.expect(RuntimeException.class);

        avroService = new AvroService("schemas/non_existing_schema.avsc");
    }
}
