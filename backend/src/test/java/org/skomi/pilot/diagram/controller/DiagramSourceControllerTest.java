package org.skomi.pilot.diagram.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skomi.pilot.ui.controller.DiagramSourceController;
import org.skomi.pilot.ui.model.DiagramSourceDto;
import org.skomi.pilot.ui.service.DiagramSourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DiagramSourceControllerTest {

    @Mock
    private DiagramSourceService diagramSourceService;

    @Mock
    private Authentication authentication;

    /**
     * Tests that the getDiagramSource method successfully retrieves an existing diagram source.
     */
    @Test
    void shouldReturnDiagramSourceWhenExists() {
        // Given
        DiagramSourceController controller = new DiagramSourceController(diagramSourceService);
        String username = "user1";
        given(authentication.getName()).willReturn(username);

        DiagramSourceDto expectedDto = new DiagramSourceDto("diagram content");
        given(diagramSourceService.getDiagram(username)).willReturn(Optional.of(expectedDto));

        // When
        ResponseEntity<DiagramSourceDto> response = controller.getDiagramSource(authentication);

        // Then
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(expectedDto));
        then(diagramSourceService).should().getDiagram(username);
    }

    /**
     * Tests that the getDiagramSource method returns 404 when no diagram source is found.
     */
    @Test
    void shouldReturnNotFoundWhenDiagramSourceNotExists() {
        // Given
        DiagramSourceController controller = new DiagramSourceController(diagramSourceService);
        String username = "user1";
        given(authentication.getName()).willReturn(username);
        given(diagramSourceService.getDiagram(username)).willReturn(Optional.empty());

        // When
        ResponseEntity<DiagramSourceDto> response = controller.getDiagramSource(authentication);

        // Then
        assertThat(response.getStatusCodeValue(), is(404));
        assertThat(response.getBody(), is(nullValue()));
        then(diagramSourceService).should().getDiagram(username);
    }

    /**
     * Tests that the saveDiagramSource method successfully saves a valid diagram source.
     */
    @Test
    void shouldSaveDiagramSourceSuccessfully() {
        // Given
        DiagramSourceController controller = new DiagramSourceController(diagramSourceService);
        String username = "user1";
        DiagramSourceDto validDto = new DiagramSourceDto("diagram content");
        given(authentication.getName()).willReturn(username);
        given(diagramSourceService.saveDiagram(username, validDto)).willReturn(validDto);

        // When
        ResponseEntity<DiagramSourceDto> response = controller.saveDiagramSource(validDto, authentication);

        // Then
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(validDto));
        then(diagramSourceService).should().saveDiagram(username, validDto);
    }
}