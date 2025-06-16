package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.ui.model.DiagramSource;
import org.skomi.pilot.ui.model.DiagramSourceDto;
import org.skomi.pilot.ui.repository.DiagramSourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for managing diagram sources.
 */
@Service
@RequiredArgsConstructor
public class DiagramSourceService {

    private final DiagramSourceRepository repository;

    /**
     * Retrieves diagram source for a specific user.
     *
     * @param userEmail the email of the user
     * @return the diagram source DTO if found
     */
    @Transactional(readOnly = true)
    public Optional<DiagramSourceDto> getDiagram(String userEmail) {
        return repository.findById(userEmail)
                .map(source -> new DiagramSourceDto(source.getDiagram()));
    }

    /**
     * Saves or updates diagram source for a specific user.
     *
     * @param userEmail the email of the user
     * @param dto the diagram source data to save
     * @return the saved diagram source DTO
     */
    @Transactional
    public DiagramSourceDto saveDiagram(String userEmail, DiagramSourceDto dto) {
        DiagramSource source = new DiagramSource(userEmail, dto.diagram());
        DiagramSource saved = repository.createOrUpdate(source);
        return new DiagramSourceDto(saved.getDiagram());
    }
}