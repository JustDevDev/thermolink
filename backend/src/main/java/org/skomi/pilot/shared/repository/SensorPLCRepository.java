package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.ConnectedSensorDTO;
import org.skomi.pilot.shared.model.SensorPlc;
import org.skomi.pilot.shared.model.SensorPlcWithPlace;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface SensorPLCRepository extends CrudRepository<SensorPlc, UUID>, SensorPLCRepositoryCustom {

    /**
     * Finds all sensor-PLC mappings associated with a specific PLC ID, including place information
     * for each sensor associated with the given PLC.
     *
     * @param plcId the unique identifier of the PLC for which sensor-PLC mappings are to be retrieved
     * @return a list of {@link SensorPlcWithPlace} objects containing sensor-PLC mapping details
     *         and the associated place information
     */
    @Query("""
            SELECT sp.*, s.place_id as place FROM sensor_plc sp
            JOIN sensor s ON sp.sensor_id = s.id
            WHERE sp.plc_id = :plcId
            """)
    List<SensorPlcWithPlace> findSensorPlcsByPlcId(UUID plcId);

    /**
     * Retrieves a list of sensor-PLC mappings associated with a specific user ID.
     *
     * @param userId the unique identifier of the user whose sensor-PLC mappings are to be retrieved
     * @return a list of {@link SensorPlc} objects containing sensor-PLC mappings associated with the given user
     */
    List<SensorPlc> findSensorPlcsByUserId(UUID userId);

    /**
     * Retrieves a list of sensors connected to a specific PLC that is associated with a particular user.
     *
     * @param userId the unique identifier of the user whose connected sensors are to be retrieved
     * @param plcId the unique identifier of the PLC for which connected sensors are to be retrieved
     * @return a list of {@link ConnectedSensorDTO} objects, each representing a sensor connected to the specified PLC
     *         and including the sensor ID, port, and associated place information
     */
    @Query("""
            SELECT sp.sensor_id as id, sp.port, s.place_id as place FROM sensor_plc sp
                JOIN sensor s ON s.id = sp.sensor_id
            WHERE sp.user_id = :userId AND sp.plc_id = :plcId
            """)
    List<ConnectedSensorDTO> findAllConnectedSensorsOnUserPlc(UUID userId, UUID plcId);
}