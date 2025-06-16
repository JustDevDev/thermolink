package org.skomi.pilot.diagram.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skomi.pilot.shared.event.WsckSessionOpenedEvent;
import org.skomi.pilot.ui.listener.DiagramListener;
import org.skomi.pilot.ui.service.SensorUpdateDistributor;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DiagramListenerTest {

    @InjectMocks
    private DiagramListener diagramListener;

    @Mock
    private SensorUpdateDistributor sensorUpdateDistributor;

    /**
     * Test to verify that the onUserWsckSessionOpened method
     * calls the findAndSendToUserAllItsSensors method of SensorUpdateDistributor
     * when a valid WsckSessionOpenedEvent is received.
     */
    @Test
    void shouldSendSensorDataToUserWhenSessionOpened() {
        // given
        String userEmail = "test@example.com";
        WsckSessionOpenedEvent event = new WsckSessionOpenedEvent(this, userEmail);

        // when
        diagramListener.onUserWsckSessionOpened(event);

        // then
        then(sensorUpdateDistributor).should(times(1)).findAndSendToUserAllItsSensors(userEmail);
    }

    /**
     * Test to verify that the onUserWsckSessionOpened method
     * does not throw any exceptions when WsckSessionOpenedEvent has a null userEmail.
     */
    @Test
    void shouldHandleNullUserEmailInSessionOpenedEvent() {
        // given
        String userEmail = null;
        WsckSessionOpenedEvent event = new WsckSessionOpenedEvent(this, userEmail);

        // when
        diagramListener.onUserWsckSessionOpened(event);

        // then
        then(sensorUpdateDistributor).should(times(1)).findAndSendToUserAllItsSensors(userEmail);
    }

    /**
     * Test to verify that the onUserWsckSessionOpened method
     * handles an empty userEmail gracefully.
     */
    @Test
    void shouldHandleEmptyUserEmailInSessionOpenedEvent() {
        // given
        String userEmail = "";
        WsckSessionOpenedEvent event = new WsckSessionOpenedEvent(this, userEmail);

        // when
        diagramListener.onUserWsckSessionOpened(event);

        // then
        then(sensorUpdateDistributor).should(times(1)).findAndSendToUserAllItsSensors(userEmail);
    }
}