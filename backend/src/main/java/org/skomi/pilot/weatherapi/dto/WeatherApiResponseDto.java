package org.skomi.pilot.weatherapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Weather information response")
public class WeatherApiResponseDto {
    @Schema(description = "Location details")
    private LocationDto location;

    @Schema(description = "Current weather conditions")
    private CurrentWeatherDto current;
}

