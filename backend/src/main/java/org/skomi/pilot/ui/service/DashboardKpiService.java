package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.model.DashboardKpisDto;
import org.skomi.pilot.shared.model.PlaceHistory;
import org.skomi.pilot.shared.model.SensorWithData;
import org.skomi.pilot.shared.repository.PlaceHistoryRepository;
import org.skomi.pilot.shared.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardKpiService {

    private final PlaceHistoryRepository placeHistoryRepository;
    private final SensorRepository sensorRepository;

    /**
     * Retrieves key performance indicators (KPIs) for the dashboard of a specific user, including
     * the number of active and inactive sensors, as well as the highest and lowest temperatures logged
     * by the user's sensors for the current day.
     *
     * @param userId the unique identifier of the user for whom the KPIs are to be retrieved
     * @return a {@code DashboardKpisDto} object containing the KPIs, including the number of active
     * and inactive sensors, the information about the highest temperature recorded, and the
     * information about the lowest temperature recorded
     */
    public DashboardKpisDto getKpis(UUID userId) {
        Optional<PlaceHistory> highestTemp = placeHistoryRepository.findHighestTemperatureToday(userId);
        Optional<PlaceHistory> lowestTemp = placeHistoryRepository.findLowestTemperatureToday(userId);
        List<SensorWithData> sensors = sensorRepository.getAllAssociatedToUser(userId);
        long activeSensors = sensors.stream().filter(sensorWithData -> sensorWithData.getPlace() != null && !sensorWithData.getPlace().isBlank()).count();
        long inactiveSensors = sensors.size() - activeSensors;

        return new DashboardKpisDto(
                activeSensors,
                inactiveSensors,
                highestTemp.filter(temp -> temp.getTemperature() != null)
                        .map(DashboardKpisDto.TemperatureKpiDto::new)
                        .orElse(new DashboardKpisDto.TemperatureKpiDto()),
                lowestTemp.filter(temp -> temp.getTemperature() != null)
                        .map(DashboardKpisDto.TemperatureKpiDto::new)
                        .orElse(new DashboardKpisDto.TemperatureKpiDto())
        );
    }
}
