package org.skomi.pilot.shared.repository.impl;

import org.skomi.pilot.shared.model.Sensor;
import org.skomi.pilot.shared.repository.SensorRepositoryCustom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SensorRepositoryCustomImpl implements SensorRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public SensorRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Sensor> upsertAll(List<Sensor> sensors) {
        String sql = """
                INSERT INTO sensor (id, place_id)
                VALUES (?, ?)
                ON CONFLICT (id) DO UPDATE
                SET place_id = EXCLUDED.place_id
                """;

        jdbcTemplate.batchUpdate(sql, sensors, 100, (ps, sensor) -> {
            ps.setObject(1, sensor.getId());                // UUID primary key
            ps.setString(2, sensor.getPlaceId());
        });
        return sensors;
    }
}