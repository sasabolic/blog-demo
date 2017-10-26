package io.sixhours.blog.demo;

/**
 * Service used to send events.
 *
 * @author Sasa Bolic
 */
public interface EventService {

    /**
     * Sends event.
     *
     * @param value the value
     */
    void sendEvent(Event value);
}
