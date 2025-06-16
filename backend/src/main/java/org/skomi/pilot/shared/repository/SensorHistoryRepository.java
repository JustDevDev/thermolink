package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.SensorHistory;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SensorHistoryRepository extends CrudRepository<SensorHistory, UUID> {

    /**
     * Retrieves all SensorHistory records associated with the specified sensor ID.
     *
     * @param sensorId the unique identifier of the sensor whose history records are to be retrieved
     * @return a list of SensorHistory objects associated with the given sensor ID
     */
    List<SensorHistory> findAllBySensorId(UUID sensorId);

    /**
     * Retrieves the latest 10 SensorHistory records associated with the specified sensor ID,
     * ordered by their ID in descending order.
     *
     * @param sensorId the unique identifier of the sensor for which the latest 10 history records are to be retrieved
     * @return a list of up to 10 SensorHistory objects associated with the given sensor ID, ordered by their ID in descending order
     */
    @Query("SELECT * FROM sensor_history WHERE sensor_id = :sensorId ORDER BY id DESC LIMIT 10")
    List<SensorHistory> findLatest10BySensorId(UUID sensorId);
}
