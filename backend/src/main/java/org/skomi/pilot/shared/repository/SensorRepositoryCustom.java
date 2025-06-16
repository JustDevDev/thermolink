// File: SensorRepositoryCustom.java
package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.Sensor;

import java.util.List;

public interface SensorRepositoryCustom {
    /**
     * Performs an upsert operation for a list of sensors. For each sensor in the
     * input list, the method inserts a new record if the sensor does not exist
     * in the database, or updates the existing record if it does.
     *
     * @param sensors the list of {@link Sensor} objects to be upserted, where
     *                each sensor contains data to be inserted or updated
     * @return the list of {@link Sensor} objects that have been upserted, with
     *         any database-managed fields (e.g., generated identifiers or updated timestamps)
     *         populated as needed
     */
    List<Sensor> upsertAll(List<Sensor> sensors);
}