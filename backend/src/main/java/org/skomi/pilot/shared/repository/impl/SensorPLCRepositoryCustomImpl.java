// File: SensorPLCRepositoryCustomImpl.java
package org.skomi.pilot.shared.repository.impl;

import org.skomi.pilot.shared.model.SensorPlc;
import org.skomi.pilot.shared.repository.SensorPLCRepositoryCustom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SensorPLCRepositoryCustomImpl implements SensorPLCRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public SensorPLCRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void upsertAll(List<SensorPlc> sensorPlcs) {
        String sql = """
                INSERT INTO sensor_plc (user_id, sensor_id, plc_id, port)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (port, plc_id) DO UPDATE
                SET user_id = EXCLUDED.user_id,
                    sensor_id = EXCLUDED.sensor_id
                """;

        jdbcTemplate.batchUpdate(sql, sensorPlcs, 100, (ps, sensorPlc) -> {
            ps.setObject(1, sensorPlc.getUserId());
            ps.setObject(2, sensorPlc.getSensorId());
            ps.setObject(3, sensorPlc.getPlcId());
            ps.setObject(4, sensorPlc.getPort());
        });
    }
}