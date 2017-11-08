package io.sixhours.blog.demo.command;

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
