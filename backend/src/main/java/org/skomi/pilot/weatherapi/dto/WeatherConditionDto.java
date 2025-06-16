package org.skomi.pilot.weatherapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Weather condition information")
public class WeatherConditionDto {
    @Schema(description = "Text description of weather condition", example = "Partly cloudy")
    private String text;

    @Schema(description = "URL of the weather condition icon",
            example = "//cdn.weatherapi.com/weather/64x64/day/116.png")
    private String icon;

    @Schema(description = "Weather condition code", example = "1003")
    private Integer code;
}
