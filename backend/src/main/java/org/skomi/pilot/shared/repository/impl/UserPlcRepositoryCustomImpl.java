// File: UserPlcRepositoryCustomImpl.java
package org.skomi.pilot.shared.repository.impl;

import org.skomi.pilot.shared.model.UserPlc;
import org.skomi.pilot.shared.repository.UserPlcRepositoryCustom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserPlcRepositoryCustomImpl implements UserPlcRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public UserPlcRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void upsertAll(List<UserPlc> userPlcs) {
        String sql = """
                INSERT INTO user_plc (plc_id, user_id)
                VALUES (?, ?)
                ON CONFLICT (plc_id, user_id) DO UPDATE
                SET id = EXCLUDED.id
                """;

        jdbcTemplate.batchUpdate(sql, userPlcs, 100, (ps, userPlc) -> {
            ps.setObject(1, userPlc.getPlcId());
            ps.setObject(2, userPlc.getUserId());
        });
    }
}