package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.Plc;

import java.util.List;

public interface PlcRepositoryCustom {
    /**
     * Performs an upsert operation on a list of PLC entities. For each PLC in the provided list,
     * if a matching entity already exists in the database (based on its unique identifier), it will be
     * updated with the new data. If no match is found, a new entity will be inserted into the database.
     *
     * @param plcs the list of {@link Plc} entities to be upserted. Each entity must contain a unique identifier,
     *             which will be used to determine whether to insert or update the entity.
     * @return a list of {@link Plc} entities that have been successfully upserted. The returned entities may include
     *         additional updates or generated values as a result of database operations.
     */
    List<Plc> upsertAll(List<Plc> plcs);
}