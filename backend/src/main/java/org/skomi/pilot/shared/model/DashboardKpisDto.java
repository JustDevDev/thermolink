package org.skomi.pilot.shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class DashboardKpisDto {
    private long activeSensors;
    private long inactiveSensors;
    private TemperatureKpiDto todayHighTemperature;
    private TemperatureKpiDto todayLowTemperature;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemperatureKpiDto {
        private String place;
        private double temperature;

        public TemperatureKpiDto(PlaceHistory placeHistory) {
            this.place = placeHistory.getPlaceId();
            this.temperature = placeHistory.getTemperature();
        }
    }
}