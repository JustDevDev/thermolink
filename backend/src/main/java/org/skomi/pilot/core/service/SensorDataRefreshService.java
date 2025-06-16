package org.skomi.pilot.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.core.publisher.CoreEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service responsible for periodically triggering the refresh of sensor data.
 * It uses {@link CoreEventPublisher} to publish events for refreshing sensor data.
 * The schedule for triggering refresh is configurable via the property {@code sensor.data.refresh.rate}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SensorDataRefreshService {

    private final CoreEventPublisher coreEventPublisher;

    /**
     * Periodically triggers sensor data refresh by publishing an event.
     */
    @Scheduled(cron = "0 0 * * * ?")
    private void refreshSensors() {
        log.info("Calling for refresh sensor data.");
        coreEventPublisher.callForRefreshSensors();
    }
}
