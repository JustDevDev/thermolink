package org.skomi.pilot.core.publisher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skomi.pilot.shared.event.RefreshAllPlacesEvent;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CoreEventPublisherTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private CoreEventPublisher coreEventPublisher;

    /**
     * Tests that the `callForRefreshSensors` method correctly publishes a `RefreshAllPlacesEvent`.
     */
    @Test
    void shouldPublishRefreshAllPlacesEvent() {
        // given

        // when
        coreEventPublisher.callForRefreshSensors();

        // then
        then(applicationEventPublisher)
                .should()
                .publishEvent(any(RefreshAllPlacesEvent.class));
    }
}