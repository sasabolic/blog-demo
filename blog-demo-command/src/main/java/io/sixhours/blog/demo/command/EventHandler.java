package io.sixhours.blog.demo.command;

import io.sixhours.blog.demo.common.Event;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * The handler used in a chain.
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
    ProducerRecord encode(Event event);

}