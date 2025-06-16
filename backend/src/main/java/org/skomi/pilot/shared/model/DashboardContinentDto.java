package org.skomi.pilot.shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardContinentDto {
    private String continent;
    private long count;
}