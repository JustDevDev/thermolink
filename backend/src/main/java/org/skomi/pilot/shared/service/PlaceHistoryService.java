package org.skomi.pilot.shared.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.model.Place;
import org.skomi.pilot.shared.model.PlaceHistory;
import org.skomi.pilot.shared.repository.PlaceHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing place history records.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceHistoryService {

    private final PlaceHistoryRepository placeHistoryRepository;

    /**
     * Saves place history records from a list of places.
     *
     * @param places the list of places to create history records from
     */
    public void saveAllFromPlaces(List<Place> places) {
        List<PlaceHistory> placeHistories = places.stream().map(PlaceHistory::new).toList();
        placeHistoryRepository.saveAll(placeHistories);
    }

    /**
     * Saves a list of place history records.
     *
     * @param placeHistories the list of place histories to save
     */
    public void saveAll(List<PlaceHistory> placeHistories) {
        placeHistoryRepository.saveAll(placeHistories);
    }

    /**
     * Retrieves all place history records.
     *
     * @return a list of all place history records
     */
    public List<PlaceHistory> findAll() {
        return (List<PlaceHistory>) placeHistoryRepository.findAll();
    }

    /**
     * Finds all history records for a specific place.
     *
     * @param placeId the ID of the place to retrieve history for
     * @return a list of place history records for the given place ID
     */
    public List<PlaceHistory> findAllById(String placeId) {
        return placeHistoryRepository.findAllByPlaceId(placeId);
    }

    /**
     * Finds a place history record by its ID.
     *
     * @param id the UUID of the place history record
     * @return an Optional containing the place history record if found
     */
    public Optional<PlaceHistory> findById(UUID id) {
        return placeHistoryRepository.findById(id);
    }

    /**
     * Saves a single place history record.
     *
     * @param placeHistory the place history record to save
     * @return the saved place history record
     */
    public PlaceHistory save(PlaceHistory placeHistory) {
        return placeHistoryRepository.save(placeHistory);
    }

    /**
     * Deletes a place history record by its ID.
     *
     * @param id the UUID of the place history record to delete
     */
    public void deleteById(UUID id) {
        placeHistoryRepository.deleteById(id);
    }
}