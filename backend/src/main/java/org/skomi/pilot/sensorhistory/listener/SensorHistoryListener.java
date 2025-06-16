package org.skomi.pilot.sensorhistory.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.event.PlaceActualWeatherDataEvent;
import org.skomi.pilot.shared.event.PlaceWeatherDataHistoryEvent;
import org.skomi.pilot.shared.model.PlaceHistory;
import org.skomi.pilot.shared.service.PlaceHistoryService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handles sensor history for current sensors.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SensorHistoryListener {

    private final PlaceHistoryService placeHistoryService;

    /**
     * Listens for sensor updates and persists data events.
     *
     * @param event Sensor events
     */
    @EventListener
    public void handlePlaceHistory(PlaceActualWeatherDataEvent event) {
        placeHistoryService.saveAllFromPlaces(event.getPlaces());
    }

    /**
     * Listens for sensor history data and persists data events.
     *
     * @param event Sensor history events
     */
    @EventListener
    public void handlePlaceHistoryData(PlaceWeatherDataHistoryEvent event) {
        List<PlaceHistory> histories = event.getTemperatures()
                .stream()
                .map(temperature -> new PlaceHistory(temperature, event.getPlace()))
                .toList();

        AtomicInteger hours = new AtomicInteger(0);
        histories.forEach(history -> history.setUpdatedAt(OffsetDateTime.now().minusHours(hours.getAndIncrement())));

        placeHistoryService.saveAll(histories);
    }
}
