package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.model.ConnectedPlcDTO;
import org.skomi.pilot.shared.model.SensorWithData;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.model.UserSensor;
import org.skomi.pilot.shared.repository.PlaceHistoryRepository;
import org.skomi.pilot.shared.repository.PlcRepository;
import org.skomi.pilot.shared.repository.SensorRepository;
import org.skomi.pilot.shared.repository.UserSensorRepository;
import org.skomi.pilot.shared.service.UserService;
import org.skomi.pilot.ui.model.SensorTableResponse;
import org.skomi.pilot.ui.model.TableResponse;
import org.skomi.pilot.ui.model.TemperatureRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SensorTableService {

    private final UserSensorRepository userSensorRepository;
    private final UserService userService;
    private final SensorRepository sensorRepository;
    private final PlcRepository plcRepository;
    private final PlaceHistoryRepository placeHistoryRepository;

    /**
     * Retrieves a paginated list of sensors associated with a specific user identified by their email.
     * The method constructs a response with detailed information about the sensors and their connected PLCs.
     *
     * @param pageable  the pagination information, including page number and size
     * @param userEmail the email address of the user whose sensors are being retrieved
     * @return a {@code TableResponse<SensorTableResponse>} containing the list of sensors associated with the user,
     * along with pagination details and additional information about connected PLCs
     */
    public TableResponse<SensorTableResponse> getSensorsOnUser(Pageable pageable, String userEmail) {

        Optional<User> user = userService.findUserByEmail(userEmail);
        Page<UserSensor> userSensors = userSensorRepository.findAllByUserId(user.get().getId(), pageable);
        List<UUID> sensorIds = userSensors.stream().map(UserSensor::getSensorId).toList();

        sensorIds = sensorIds.isEmpty() ? List.of(UUID.fromString("00000000-0000-0000-0000-000000000000")) : sensorIds;
        List<SensorWithData> sensors = sensorRepository.findAllWithDataById(sensorIds);
        TableResponse<SensorTableResponse> response = new TableResponse<>();

        List<SensorTableResponse> sensorTableData = sensors.stream().map(sensor -> new SensorTableResponse(
                sensor,
                getConnectedPlcs(user.get().getId(), sensor.getId()),
                getLastTenTemperatureRecords(sensor.getPlace())
        )).toList();

        response.setSize(userSensors.getSize());
        response.setCurrentPage(userSensors.getNumber());
        response.setTotalPages(userSensors.getTotalPages());
        response.setTotalElements((int) userSensors.getTotalElements());
        response.setContent(sensorTableData);
        return response;
    }

    /**
     * Retrieves a list of ConnectedPlcDTO objects associated with a specific sensor.
     *
     * @param sensorId the unique identifier of the sensor for which the connected PLCs are to be retrieved
     * @return a list of {@code ConnectedPlcDTO} representing the PLCs connected to the specified sensor
     */
    public List<ConnectedPlcDTO> getConnectedPlcs(UUID userId, UUID sensorId) {
        return plcRepository.getAllConnectedPlcsOnUserAndSensor(userId, sensorId);
    }

    /**
     * Retrieves the last ten temperature records for a specific sensor.
     *
     * @param placeId the unique identifier of the sensor for which the last ten temperature records are to be retrieved
     * @return a list of {@code TemperatureRecordDTO} containing the temperature values and their corresponding timestamps
     */
    public List<TemperatureRecordDTO> getLastTenTemperatureRecords(String placeId) {
        return placeHistoryRepository
                .findLatest10BySensorId(placeId)
                .stream()
                .filter(history -> history.getTemperature() != null)
                .map(history -> new TemperatureRecordDTO(history.getTemperature(), history.getUpdatedAt().toInstant()))
                .toList();
    }
}