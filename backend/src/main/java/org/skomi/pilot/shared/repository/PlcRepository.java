package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.ConnectedPlcDTO;
import org.skomi.pilot.shared.model.DashboardPlcsDto;
import org.skomi.pilot.shared.model.Plc;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for PLC entity operations.
 */
@Repository
public interface PlcRepository extends CrudRepository<Plc, UUID>, PlcRepositoryCustom {

    /**
     * Retrieves a list of PLCs connected to a specific user and sensor.
     * <p>
     * This method executes a query to join the PLC and sensor_plc tables,
     * filtering by the provided user ID and sensor ID. The data retrieved
     * includes the name of the PLC, the port it is connected to, and its ID.
     *
     * @param userId   the unique identifier of the user associated with the PLCs
     * @param sensorId the unique identifier of the sensor associated with the PLCs
     * @return a list of {@link ConnectedPlcDTO} objects, each containing the PLC name, port, and ID
     */
    @Query("""
            SELECT p.name, ps.port, ps.id FROM plc p
                JOIN sensor_plc ps ON p.id = ps.plc_id
            WHERE ps.sensor_id = :sensorId AND ps.user_id = :userId
            """)
    List<ConnectedPlcDTO> getAllConnectedPlcsOnUserAndSensor(UUID userId, UUID sensorId);

    /**
     * Retrieves a list of PLCs associated with a specific user.
     * <p>
     * This method executes a query to join the PLC and user_plc tables,
     * filtering by the provided user ID. The data retrieved includes the
     * ID and name of the PLCs.
     *
     * @param userId the unique identifier of the user associated with the PLCs
     * @return a list of {@link DashboardPlcsDto} objects, each containing the ID and name of a PLC
     */
    @Query("""
            SELECT p.id as id, p.name as name FROM plc p
                JOIN user_plc up ON p.id = up.plc_id
            WHERE up.user_id = :userId
            """)
    List<DashboardPlcsDto> getAllPlcsOnUser(UUID userId);
}