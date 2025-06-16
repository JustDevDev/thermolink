package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.model.DashboardContinentDto;
import org.skomi.pilot.shared.repository.UserSensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardSensorService {

    private final UserSensorRepository userSensorRepository;

    /**
     * Retrieves a list of continents and their respective count of sensors associated with
     * the sensors belonging to a specific user.
     *
     * @param userId the unique identifier of the user whose sensors' associated continents are to be retrieved
     * @return a list of {@link DashboardContinentDto} containing the name of each continent and the count of sensors associated with it
     */
    public List<DashboardContinentDto> getContinentCountsOnUserSensor(UUID userId) {
        return userSensorRepository.findAllContinentsOnSensorsOfUser(userId)
                .stream()
                .filter(dto -> dto.getContinent() != null)
                .toList();
    }
}
