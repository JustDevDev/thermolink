// File: UserPlcRepositoryCustom.java
package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.UserPlc;

import java.util.List;

public interface UserPlcRepositoryCustom {
    /**
     * Inserts or updates a list of UserPlc entities in the database.
     * This method performs an upsert operation, meaning each entity in the list
     * will either be inserted as new or updated if it already exists based on its unique identifier.
     *
     * @param userPlcs the list of UserPlc entities to be upserted
     */
    void upsertAll(List<UserPlc> userPlcs);
}