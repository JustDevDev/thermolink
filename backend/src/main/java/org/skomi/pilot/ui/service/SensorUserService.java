package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.model.SensorWithData;
import org.skomi.pilot.shared.repository.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class SensorUserService {

    private final SensorRepository sensorRepository;

    /**
     * Retrieves the list of sensors associated with a specific user based on the given user ID.
     *
     * @param userId the unique identifier of the user whose associated sensors are to be retrieved
     * @return a list of sensors associated with the specified user
     */
    public List<SensorWithData> getSensorsAssociatedToUser(UUID userId) {
        return sensorRepository.getAllAssociatedToUser(userId);
    }
}
