// File: UserSensorRepositoryCustomImpl.java
package org.skomi.pilot.shared.repository.impl;

import org.skomi.pilot.shared.model.UserSensor;
import org.skomi.pilot.shared.repository.UserSensorRepositoryCustom;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserSensorRepositoryCustomImpl implements UserSensorRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public UserSensorRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void upsertAll(List<UserSensor> userSensors) {
        String sql = """
                INSERT INTO user_sensor (user_id, sensor_id)
                VALUES (?, ?)
                ON CONFLICT (user_id, sensor_id) DO UPDATE
                SET id = EXCLUDED.id
                """;

        jdbcTemplate.batchUpdate(sql, userSensors, 100, (ps, userSensor) -> {
            ps.setObject(1, userSensor.getUserId());
            ps.setObject(2, userSensor.getSensorId());
        });
    }
}