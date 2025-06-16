package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.model.ConnectedSensorDTO;
import org.skomi.pilot.shared.model.Plc;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.model.UserPlc;
import org.skomi.pilot.shared.repository.PlcRepository;
import org.skomi.pilot.shared.repository.SensorPLCRepository;
import org.skomi.pilot.shared.repository.UserPlcRepository;
import org.skomi.pilot.shared.service.UserService;
import org.skomi.pilot.ui.model.PlcTableResponse;
import org.skomi.pilot.ui.model.TableResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlcTableService {

    private final UserService userService;
    private final UserPlcRepository userPlcRepository;
    private final SensorPLCRepository sensorPLCRepository;
    private final PlcRepository plcRepository;

    /**
     * Retrieves a paginated and detailed table response of PLCs associated with a user identified by their email.
     *
     * @param pageable  the pagination information including page size and current page
     * @param userEmail the email of the user whose associated PLCs are to be retrieved
     * @return a TableResponse containing details of PLCs wrapped in PlcTableResponse objects,
     * along with pagination metadata
     */
    public TableResponse<PlcTableResponse> getPlcs(Pageable pageable, String userEmail) {

        TableResponse<PlcTableResponse> response = new TableResponse<>();

        Optional<User> user = userService.findUserByEmail(userEmail);
        Page<UserPlc> userPlcs = userPlcRepository.findAllByUserId(user.get().getId(), pageable);

        Iterable<UUID> plcIds = userPlcs.stream().map(UserPlc::getPlcId).toList();
        List<Plc> plcs = (List<Plc>) plcRepository.findAllById(plcIds);
        List<PlcTableResponse> plcTableData = plcs.stream().map(
                plc ->
                        new PlcTableResponse(
                                plc,
                                getConnectedSensors(plc.getId())
                        )).toList();

        // fill up data
        response.setSize(userPlcs.getSize());
        response.setCurrentPage(userPlcs.getNumber());
        response.setTotalPages(userPlcs.getTotalPages());
        response.setTotalElements((int) userPlcs.getTotalElements());
        response.setContent(plcTableData);
        return response;
    }

    /**
     * Retrieves a list of connected sensors for a specified PLC.
     *
     * @param plcId the UUID of the PLC whose connected sensors are to be retrieved
     * @return a list of ConnectedSensorDTO representing the connected sensors of the given PLC
     */
    public List<ConnectedSensorDTO> getConnectedSensors(UUID plcId) {
        return sensorPLCRepository.findSensorPlcsByPlcId(plcId)
                .stream()
                .distinct()
                .map(ConnectedSensorDTO::new)
                .toList();
    }
}