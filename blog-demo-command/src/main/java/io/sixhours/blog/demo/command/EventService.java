package io.sixhours.blog.demo.command;

import io.sixhours.blog.demo.common.Event;

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
