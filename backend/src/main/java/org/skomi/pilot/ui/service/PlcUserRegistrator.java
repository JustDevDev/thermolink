package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.model.Plc;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.model.UserPlc;
import org.skomi.pilot.shared.repository.UserPlcRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Registering sensor with plc connections
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlcUserRegistrator {

    private final UserPlcRepository userPlcRepository;

    /**
     * Persists the associations between a list of PLC objects and a user in the database.
     * Each PLC in the list will be associated with the given user, and these associations
     * will be saved into the system.
     *
     * @param savedPlcs the list of PLC objects to be associated with the user
     * @param user      the user to whom the PLCs will be associated
     */
    public void registerPLCToUser(List<Plc> savedPlcs, User user) {
        List<UserPlc> userPlcAssociations = savedPlcs.stream()
                .map(plc -> {
                    UserPlc userPlc = new UserPlc();

                    userPlc.setUserId(user.getId());
                    userPlc.setPlcId(plc.getId());

                    return userPlc;
                })
                .collect(Collectors.toList());

        userPlcRepository.upsertAll(userPlcAssociations);
    }
}