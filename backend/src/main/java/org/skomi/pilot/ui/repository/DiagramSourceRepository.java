package org.skomi.pilot.ui.repository;

import org.skomi.pilot.ui.model.DiagramSource;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for DiagramSource entity operations.
 */
public interface DiagramSourceRepository extends CrudRepository<DiagramSource, String> {

    /**
     * Creates or updates a DiagramSource record.
     *
     * @param source The DiagramSource object to save or update.
     * @return The saved or updated DiagramSource instance.
     */

    @Query("INSERT INTO diagram_source (user_email, diagram) " +
           "VALUES (:#{#source.userEmail}, :#{#source.diagram}) " +
           "ON CONFLICT (user_email) DO UPDATE SET diagram = :#{#source.diagram} " +
           "RETURNING *")
    DiagramSource createOrUpdate(DiagramSource source);
}