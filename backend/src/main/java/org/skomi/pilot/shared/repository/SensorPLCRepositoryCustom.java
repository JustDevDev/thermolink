// File: SensorPLCRepositoryCustom.java
package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.SensorPlc;

import java.util.List;

public interface SensorPLCRepositoryCustom {
    /**
     * Performs an upsert operation for a list of {@link SensorPlc} entities.
     * For each entity in the list, if a record with a matching ID already exists,
     * it will be updated; otherwise, a new record will be inserted.
     *
     * @param sensorPlcs the list of {@link SensorPlc} entities to be upserted
     */
    void upsertAll(List<SensorPlc> sensorPlcs);
}