package io.sixhours.blog.demo.query;

import io.sixhours.blog.demo.common.Event;

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
    void handle(Event event);

}