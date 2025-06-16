package org.skomi.pilot.diagram.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skomi.pilot.ui.controller.DiagramDataController;
import org.skomi.pilot.ui.model.DiagramDTO;
import org.skomi.pilot.ui.model.PlcDTO;
import org.skomi.pilot.ui.model.SensorDTO;
import org.skomi.pilot.ui.service.DiagramService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DiagramDataControllerTest {

    @Mock
    private DiagramService diagramService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DiagramDataController diagramDataController;

    private final UUID sensorId = UUID.randomUUID();
    private final UUID sensor2Id = UUID.randomUUID();
    private final UUID plcId = UUID.randomUUID();
    private final UUID plcId2 = UUID.randomUUID();
    private final String testPlace = "testPlace";
    @SuppressWarnings("FieldCanBeLocal")
    private final String testPlace2 = "testPlace2";

    /**
     * Tests that valid DiagramDTO is saved successfully and returns 200 OK with the saved DiagramDTO.
     */
    @Test
    void shouldSaveDiagramDataSuccessfully() {
        // Given
        List<SensorDTO> sensors = List.of(new SensorDTO(sensorId, testPlace, List.of()), new SensorDTO(sensor2Id, testPlace2, List.of()));
        List<PlcDTO> plcs = List.of(new PlcDTO(plcId, ""), new PlcDTO(plcId2, ""));
        DiagramDTO diagramDTO = new DiagramDTO(sensors, plcs);

        String username = "testUser";
        given(authentication.getName()).willReturn(username);
        given(diagramService.saveDiagramData(username, diagramDTO)).willReturn(diagramDTO);

        // When
        ResponseEntity<DiagramDTO> response = diagramDataController.saveDiagramData(diagramDTO, authentication);

        // Then
        verify(diagramService).saveDiagramData(username, diagramDTO);
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(diagramDTO));
    }

    /**
     * Tests that null DiagramDTO returns 400 Bad Request.
     */
    @Test
    void shouldReturnBadRequestForNullDiagramDTO() {
        // When
        ResponseEntity<DiagramDTO> response = diagramDataController.saveDiagramData(null, authentication);

        // Then
        assertThat(response.getStatusCodeValue(), is(400));
    }

    /**
     * Tests that DiagramDTO with null PLC list returns 400 Bad Request.
     */
    @Test
    void shouldReturnBadRequestForNullPLCs() {
        // Given
        List<SensorDTO> sensors = List.of(new SensorDTO(sensorId, testPlace, List.of()));
        DiagramDTO diagramDTO = new DiagramDTO(sensors, null);

        // When
        ResponseEntity<DiagramDTO> response = diagramDataController.saveDiagramData(diagramDTO, authentication);

        // Then
        assertThat(response.getStatusCodeValue(), is(400));
    }

    /**
     * Tests that DiagramDTO with null sensor list returns 400 Bad Request.
     */
    @Test
    void shouldReturnBadRequestForNullSensors() {
        // Given
        List<PlcDTO> plcs = List.of(new PlcDTO(plcId, ""));
        DiagramDTO diagramDTO = new DiagramDTO(null, plcs);

        // When
        ResponseEntity<DiagramDTO> response = diagramDataController.saveDiagramData(diagramDTO, authentication);

        // Then
        assertThat(response.getStatusCodeValue(), is(400));
    }

    /**
     * Tests that DiagramDTO with empty PLCs and Sensors is saved successfully.
     */
    @Test
    void shouldSaveDiagramDataWithEmptyPLCsAndSensors() {
        // Given
        DiagramDTO diagramDTO = new DiagramDTO(Collections.emptyList(), Collections.emptyList());

        String username = "testUser";
        given(authentication.getName()).willReturn(username);
        given(diagramService.saveDiagramData(username, diagramDTO)).willReturn(diagramDTO);

        // When
        ResponseEntity<DiagramDTO> response = diagramDataController.saveDiagramData(diagramDTO, authentication);

        // Then
        verify(diagramService).saveDiagramData(username, diagramDTO);
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(diagramDTO));
    }
}