// File: UserSensorRepositoryCustom.java
package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.UserSensor;

import java.util.List;

public interface UserSensorRepositoryCustom {
    /**
     * Inserts or updates all the provided UserSensor entities in the repository.
     * If a UserSensor entity with the same identifiers already exists, it will be updated;
     * otherwise, a new entity will be inserted.
     *
     * @param userSensors a list of UserSensor objects to be upserted. Each object contains the
     *                    identifiers and data necessary for the operation.
     */
    void upsertAll(List<UserSensor> userSensors);
}