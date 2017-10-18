package io.sixhours.blog.demo.blogdemo;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

/**
 * The abstract class of handler used in a chain.
 *
 * @author Sasa Bolic
 */
public interface EventHandler {

    /**
     * Sets next handler in the chain.
     *
     * @param handler the handler
     */
    void setNext(EventHandler handler);

    /**
     * Handles request.
     *
     * @param event the event
     */
    byte[] handleRequest(Event event);

}