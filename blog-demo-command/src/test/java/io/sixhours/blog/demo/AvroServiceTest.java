package io.sixhours.blog.demo;

import com.twitter.bijection.Injection;
import org.apache.avro.generic.GenericRecord;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;

public class AvroServiceTest {

    private AvroService avroService;
    private GenericRecord record;
    private Injection<GenericRecord, byte[]> recordInjection;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        record = mock(GenericRecord.class);
        recordInjection = mock(Injection.class);
        avroService = spy(new AvroService("schemas/blog_post_created.avsc"));
    }

    @Test
    public void whenAddFieldThenInvokeRecordPut() {
        doReturn(record).when(avroService).getRecord();

        avroService.addField("id", "value");

        verify(record).put(isA(String.class), isA(Object.class));

    }

    @Test
    public void whenGetDataThenInvokeRecordInjectionApply() {
        doReturn(recordInjection).when(avroService).getRecordInjection();

        avroService.getData();

        verify(recordInjection).apply(isA(GenericRecord.class));

    }

    @Test
    public void givenNonExistingSchemaLocationWhenInstantiatedThenThrowRuntimeException() {
        exception.expect(RuntimeException.class);

        avroService = new AvroService("schemas/non_existing_schema.avsc");
    }
}
