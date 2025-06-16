package org.skomi.pilot.core.publisher;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.event.RefreshAllPlacesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Operates core logic of the application
 */
@Service
@RequiredArgsConstructor
public class CoreEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Calls for sensor refresh
     */
    public void callForRefreshSensors() {
        applicationEventPublisher.publishEvent(
                new RefreshAllPlacesEvent(this)
        );
    }
}