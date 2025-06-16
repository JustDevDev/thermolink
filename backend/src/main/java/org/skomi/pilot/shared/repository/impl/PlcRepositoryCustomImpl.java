package org.skomi.pilot.shared.repository.impl;

import org.skomi.pilot.shared.model.Plc;
import org.skomi.pilot.shared.repository.PlcRepositoryCustom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class PlcRepositoryCustomImpl implements PlcRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public PlcRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Plc> upsertAll(List<Plc> plcs) {
        String sql = """
                INSERT INTO plc (id, name)
                VALUES (?, ?)
                ON CONFLICT (id) DO UPDATE
                SET name = EXCLUDED.name
                """;

        jdbcTemplate.batchUpdate(sql, plcs, 100, (ps, plc) -> {
            ps.setObject(1, plc.getId());        // UUID primary key
            ps.setString(2, plc.getName());
        });
        return plcs;
    }
}