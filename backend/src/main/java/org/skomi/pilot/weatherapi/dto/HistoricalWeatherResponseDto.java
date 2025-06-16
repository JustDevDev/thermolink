package org.skomi.pilot.weatherapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class HistoricalWeatherResponseDto {
    private Forecast forecast;

    @Data
    public static class Forecast {
        private List<ForecastDay> forecastday;
    }

    @Data
    public static class ForecastDay {
        private Day day;
        private List<Hour> hour;
    }

    @Data
    public static class Day {
        private Double avgtemp_c;
    }

    @Data
    public static class Hour {
        private Double temp_c;
    }
}