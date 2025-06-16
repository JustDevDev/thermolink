package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.PlaceHistory;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlaceHistoryRepository extends CrudRepository<PlaceHistory, UUID> {


    /**
     * Retrieves all PlaceHistory records by the given place ID.
     *
     * @param placeId the ID of the place to retrieve the history for
     * @return a list of PlaceHistory objects associated with the given ID
     */
    @Query("""
            SELECT * FROM place_history WHERE place_id = :placeId
            """)
    List<PlaceHistory> findAllByPlaceId(String placeId);

    @Query("SELECT * FROM place_history WHERE place_id = :placeId ORDER BY id DESC LIMIT 10")
    List<PlaceHistory> findLatest10BySensorId(String placeId);

    @Query("""
            SELECT * FROM place_history ph
            WHERE DATE(ph.created_at) = CURRENT_DATE
            AND ph.temperature IS NOT NULL
            AND ph.place_id IN (
                SELECT s.place_id FROM user_sensor us
                JOIN sensor s ON s.id = us.sensor_id
                WHERE us.user_id = :userId
                )
            ORDER BY ph.temperature DESC
            LIMIT 1
            """)
    Optional<PlaceHistory> findHighestTemperatureToday(UUID userId);

    @Query("""
            SELECT * FROM place_history ph
            WHERE DATE(ph.created_at) = CURRENT_DATE
            AND ph.temperature IS NOT NULL
            AND ph.place_id IN (
                SELECT s.place_id FROM user_sensor us
                JOIN sensor s ON s.id = us.sensor_id
                WHERE us.user_id = :userId
                )
            ORDER BY ph.temperature ASC
            LIMIT 1
            """)
    Optional<PlaceHistory> findLowestTemperatureToday(UUID userId);
}