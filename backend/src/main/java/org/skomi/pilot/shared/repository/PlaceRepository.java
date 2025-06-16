package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.Place;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends CrudRepository<Place, String> {
    /**
     * Performs an upsert operation on the "place" table. If a row with the given ID already exists,
     * its ID is updated; otherwise, a new row is inserted.
     *
     * @param id the unique identifier of the place to be inserted or updated
     */
    @Modifying
    @Query("""
            INSERT INTO place (id) VALUES (:id)
            ON CONFLICT (id)
            DO UPDATE SET id = :id
            """)
    void upsert(String id);
}