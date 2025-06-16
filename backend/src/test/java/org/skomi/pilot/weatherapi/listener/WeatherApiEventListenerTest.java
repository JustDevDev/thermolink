package org.skomi.pilot.weatherapi.listener;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.skomi.pilot.shared.model.Place;
import org.skomi.pilot.ui.publisher.DiagramEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Disabled("Just for debug purposes.")
@SpringBootTest
class WeatherApiEventListenerTest {


    @Autowired
    private DiagramEventPublisher diagramEventPublisher;

    /**
     * Test calling the city on weather data evet.
     */
    @Test
    void callCeskyTesin() {
        // PREPARE
        String city = "Český Těšín";

        // TEST
        diagramEventPublisher.requestWeatherDataForPlaces(List.of(new Place(city)));
    }
}