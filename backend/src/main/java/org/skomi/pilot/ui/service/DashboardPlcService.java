package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.model.ConnectedSensorDTO;
import org.skomi.pilot.shared.model.DashboardPlcsDto;
import org.skomi.pilot.shared.model.DashboardSensorTemperatureDto;
import org.skomi.pilot.shared.model.Plc;
import org.skomi.pilot.shared.repository.PlcRepository;
import org.skomi.pilot.shared.repository.SensorPLCRepository;
import org.skomi.pilot.shared.repository.SensorRepository;
import org.skomi.pilot.ui.model.DashboardPlcWithSensorsDto;
import org.skomi.pilot.ui.model.DashboardSensorDto;
import org.skomi.pilot.ui.model.DashboardTemperatureDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardPlcService {

    private final PlcRepository plcRepository;
    private final SensorRepository sensorRepository;
    private final SensorPLCRepository sensorPLCRepository;

    /**
     * Retrieves all Programmable Logic Controllers (PLCs) associated with a specific user.
     *
     * @param userId the unique identifier of the user whose PLCs are to be retrieved
     * @return a list of {@link DashboardPlcsDto} objects representing the PLCs associated with the given user
     */
    public List<DashboardPlcsDto> getAllPlcs(UUID userId) {
        return plcRepository.getAllPlcsOnUser(userId);
    }

    /**
     * Retrieves the temperatures measured by sensors connected to a specific Programmable Logic Controller (PLC)
     * for a given user. The result includes the PLC details and a list of connected sensors with their respective
     * temperature information.
     *
     * @param plcId the unique identifier of the PLC
     * @param userId the unique identifier of the user
     * @return a {@link DashboardPlcWithSensorsDto} object containing the PLC details and the list of connected
     *         sensors, each with temperature data
     */
    @Cacheable(value = "plcs", key = "#plcId+#userId")
    public DashboardPlcWithSensorsDto getPlcTemperaturesMeasuredOnSensors(UUID plcId, UUID userId) {
        Plc plc = plcRepository.findById(plcId).orElseThrow();
        List<ConnectedSensorDTO> connectedSensorsOnPorts = sensorPLCRepository.findAllConnectedSensorsOnUserPlc(userId, plcId);
        List<UUID> sensorIds = connectedSensorsOnPorts.stream().map(ConnectedSensorDTO::id).distinct().toList();
        var temperaturesMap = getPlcTemperaturesMeasuredOnSensor(sensorIds);

        List<DashboardSensorDto> sensors = connectedSensorsOnPorts.stream().map(
                connectedSensor -> new DashboardSensorDto(
                        connectedSensor.id(),
                        connectedSensor.place(),
                        connectedSensor.port(),
                        getTemperatures(connectedSensor, temperaturesMap)
                )
        ).toList();

        return new DashboardPlcWithSensorsDto(
                plc.getId(),
                plc.getName(),
                sensors
        );
    }

    /**
     * Retrieves the temperature data associated with a given connected sensor from a map of sensor IDs to temperature data
     * and converts it into a list of DashboardTemperatureDto objects.
     *
     * @param connectedSensor the sensor for which temperature data is to be retrieved
     * @param temperaturesMap a map where the key is the sensor ID and the value is a list of temperature data
     *                         corresponding to that sensor
     * @return a list of {@link DashboardTemperatureDto} objects representing the temperature readings for the given sensor
     */
    private List<DashboardTemperatureDto> getTemperatures(ConnectedSensorDTO connectedSensor, Map<UUID, List<DashboardSensorTemperatureDto>> temperaturesMap) {

        var temperatures = temperaturesMap.get(connectedSensor.id());
        if(temperatures != null && temperatures.stream().noneMatch(temp -> temp.temperature() == null)){
            return temperaturesMap.get(connectedSensor.id())
                    .stream()
                    .map(temp -> new DashboardTemperatureDto(temp.date(), temp.temperature()))
                    .toList();
        } else {
            return List.of();
        }
    }

    /**
     * Retrieves a mapping of sensors to their respective temperature measurements.
     * For each sensor UUID provided in the input list, this method fetches the latest
     * temperature readings associated with that sensor and groups them by sensor ID.
     *
     * @param sensorIds a list of unique identifiers representing the sensors for which
     *                  temperature measurements are to be retrieved; if the list is empty,
     *                  a placeholder ID is used to ensure method execution
     * @return a map where the key is the sensor ID (UUID) and the value is a list of
     *         {@link DashboardSensorTemperatureDto} objects, each representing a sensor's
     *         temperature readings and their associated metadata
     */
    public Map<UUID, List<DashboardSensorTemperatureDto>> getPlcTemperaturesMeasuredOnSensor(List<UUID> sensorIds) {
        if (sensorIds.isEmpty()) sensorIds = List.of(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        return sensorRepository.findSensorTemperaturesBySensorIds(sensorIds)
                .stream()
                .collect(Collectors.groupingBy(DashboardSensorTemperatureDto::sensorId));
    }
}
