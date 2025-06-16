package org.skomi.pilot.shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("place_history")
public class PlaceHistory {

    @Id
    private Long id;

    @Column("condition")
    private String condition;

    @Column("created_at")
    private OffsetDateTime createdAt;

    private Double temperature;

    @Column("updated_at")
    private OffsetDateTime updatedAt;

    @Column("place_id")
    private String placeId;

    public PlaceHistory(Place place) {
        this.temperature = place.getTemperature();
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
        this.condition = place.getCondition();
        this.placeId = place.getId();
    }

    public PlaceHistory(Double temperature, String placeId) {
        this.temperature = temperature;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
        this.placeId = placeId;
        this.condition = "undefined";
    }
}