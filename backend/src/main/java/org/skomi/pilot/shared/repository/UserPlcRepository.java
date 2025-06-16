package org.skomi.pilot.shared.repository;

import org.skomi.pilot.shared.model.UserPlc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPlcRepository extends CrudRepository<UserPlc, Long>, UserPlcRepositoryCustom {
    /**
     * Finds all {@link UserPlc} entities associated with the specified user ID, with results
     * paged according to the provided Pageable parameter.
     *
     * @param userId  the unique identifier of the user whose associated UserPlc entities are to be retrieved
     * @param pageable a Pageable object specifying the paging and sorting details for the query
     * @return a Page of {@link UserPlc} entities corresponding to the given user ID,
     *         adhering to the specified paging and sorting configuration
     */
    Page<UserPlc> findAllByUserId(UUID userId, Pageable pageable);

    /**
     * Retrieves a list of UserPlc entities associated with the specified user ID.
     *
     * @param userId the unique identifier of the user whose associated UserPlc entities are to be retrieved
     * @return a list of UserPlc entities corresponding to the given user ID
     */
    List<UserPlc> findAllByUserId(UUID userId);
}
