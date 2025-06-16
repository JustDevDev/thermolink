package org.skomi.pilot.shared.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("place")
public class Place {

    @Id
    private String id;

    @Column("average_temperature")
    private Double averageTemperature;

    @Column("condition")
    private String condition;

    private String continent;

    private Double temperature;

    public Place(String id) {
        this.id = id;
    }
}