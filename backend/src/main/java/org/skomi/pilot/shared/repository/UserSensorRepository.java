package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.DashboardContinentDto;
import org.skomi.pilot.shared.model.UserSensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface UserSensorRepository extends CrudRepository<UserSensor, UUID>, UserSensorRepositoryCustom {

    /**
     * Retrieves a list of UserSensor entities where the sensor IDs match the provided sensorIds.
     *
     * @param sensorIds a list of UUIDs representing the sensor IDs to search for
     * @return a list of UserSensor entities matching the provided sensor IDs
     */
    @Query("""
            SELECT * FROM user_sensor
            WHERE sensor_id IN (:sensorIds)
            """)
    List<UserSensor> findAllBySensorIds(List<UUID> sensorIds);

    /**
     * Retrieves a list of UserSensor entities associated with a specific user.
     *
     * @param userId the unique identifier of the user whose sensors are to be retrieved
     * @return a list of UserSensor entities associated with the given userId
     */
    List<UserSensor> findAllByUserId(UUID userId);

    /**
     * Retrieves a paginated list of UserSensor entities associated with a specific user.
     *
     * @param userId   the unique identifier of the user whose sensors are to be retrieved
     * @param pageable an object specifying the pagination and sorting options for the query
     * @return a Page object containing the UserSensor entities associated with the given userId
     */
    Page<UserSensor> findAllByUserId(UUID userId, Pageable pageable);

    /**
     * Retrieves a list of continents and their respective count of sensors
     * based on sensors associated with a specific user.
     *
     * @param userId the unique identifier of the user whose sensors' associated continents are to be retrieved
     * @return a list of DashboardContinentDto containing the name of each continent and the count of sensors associated with it
     */
    @Query("""
            SELECT LOWER(p.continent) as continent, COUNT(p.continent) AS count FROM user_sensor us
            JOIN sensor s ON s.id = us.sensor_id
            JOIN place p ON p.id = s.place_id
                 WHERE us.user_id = :userId
            GROUP BY p.continent
            """)
    List<DashboardContinentDto> findAllContinentsOnSensorsOfUser(UUID userId);
}
