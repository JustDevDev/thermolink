package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.DashboardSensorTemperatureDto;
import org.skomi.pilot.shared.model.Sensor;
import org.skomi.pilot.shared.model.SensorWithData;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for {@link Sensor} entities.
 * Provides CRUD operations and custom queries for Sensor data management.
 */
@Repository
public interface SensorRepository extends CrudRepository<Sensor, UUID>, SensorRepositoryCustom {

    /**
     * Finds all sensors installed at a specific place.
     *
     * @param placeId the physical place to search for
     * @return a list of sensors at the specified place
     */
    @Query("""
            SELECT s.* FROM sensor s WHERE s.place_id = :placeId
            """)
    List<Sensor> findByPlace(String placeId);

    /**
     * Retrieves all sensors associated with a specific user along with their data.
     *
     * @param userId the unique identifier of the user whose associated sensors are to be retrieved
     * @return a list of {@link SensorWithData} objects containing sensor details and their associated data
     */
    @Query("""
            SELECT s.id, s.place_id as place, p.temperature, p.average_temperature, p.condition FROM sensor s
                     JOIN user_sensor us ON s.id = us.sensor_id
                     LEFT JOIN place p ON s.place_id = p.id
            WHERE us.user_id = :userId
            """)
    List<SensorWithData> getAllAssociatedToUser(UUID userId);

    /**
     * Retrieves a list of sensors along with their associated environmental data based on the provided sensor IDs.
     *
     * @param sensorIds the list of unique identifiers for the sensors to be retrieved
     * @return a list of {@link SensorWithData} objects, each containing sensor details and associated environmental data
     */
    @Query("""
            SELECT s.id, s.place_id as place, p.temperature, p.average_temperature, p.condition FROM sensor s
                     JOIN place p ON s.place_id = p.id
            WHERE s.id IN (:sensorIds)
            """)
    List<SensorWithData> findAllWithDataById(List<UUID> sensorIds);

    /**
     * Retrieves a list of temperature records for sensors specified by their unique identifiers.
     * The query fetches the temperatures for each sensor based on their place history, selecting the most
     * recent ten temperature measurements (excluding the latest one for variability).
     *
     * @param sensorIds the list of unique identifiers for the sensors from which temperature data will be retrieved
     * @return a list of {@link DashboardSensorTemperatureDto} objects containing sensor ID, date of the reading,
     *         and the temperature for each measurement
     */
    @Query("""
            SELECT s.id as sensor_id,
                   t.updated_at as date,
                   t.temperature as temperature
            FROM sensor s
            JOIN place_history t ON s.place_id = t.place_id
            WHERE s.id IN (:sensorIds)
            AND t.updated_at >= (
                SELECT t2.updated_at
                FROM place_history t2
                WHERE t2.place_id = s.place_id
                ORDER BY updated_at DESC
                LIMIT 1 OFFSET 9
            )
            ORDER BY s.id, t.updated_at DESC
            """)
    List<DashboardSensorTemperatureDto> findSensorTemperaturesBySensorIds(@Param("sensorIds") List<UUID> sensorIds);
}