package io.sixhours.blog.demo;

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