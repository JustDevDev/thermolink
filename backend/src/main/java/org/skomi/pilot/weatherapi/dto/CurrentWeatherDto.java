package org.skomi.pilot.weatherapi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Current weather conditions")
public class CurrentWeatherDto {
    @Schema(description = "Last update time in epoch format")
    private Long lastUpdatedEpoch;

    @Schema(description = "Last update time in readable format", example = "2025-03-15 13:00")
    private String lastUpdated;

    @JsonAlias("temp_c")
    @Schema(description = "Temperature in Celsius", example = "6.3")
    private Double tempC;

    @JsonAlias("avgtemp_c")
    @Schema(description = "Average day temperature in Celsius", example = "6.3")
    private Double averageTempC;

    @Schema(description = "Indicates if it's daytime (1) or nighttime (0)", example = "1")
    private Integer isDay;

    @Schema(description = "Weather condition details")
    private WeatherConditionDto condition;

    @Schema(description = "Wind speed in kilometers per hour", example = "23.8")
    private Double windKph;

    @Schema(description = "Wind direction in degrees", example = "72")
    private Integer windDegree;

    @Schema(description = "Wind direction as compass point", example = "ENE")
    private String windDir;

    @Schema(description = "Atmospheric pressure in millibars", example = "1015")
    private Double pressureMb;

    @Schema(description = "Atmospheric pressure in inches", example = "29.97")
    private Double pressureIn;

    @Schema(description = "Precipitation amount in millimeters", example = "0")
    private Double precipMm;

    @Schema(description = "Precipitation amount in inches", example = "0")
    private Double precipIn;

    @Schema(description = "Humidity percentage", example = "56")
    private Integer humidity;

    @Schema(description = "Cloud cover percentage", example = "75")
    private Integer cloud;

    @Schema(description = "Feels like temperature in Celsius", example = "2.3")
    private Double feelslikeC;

    @Schema(description = "Wind chill temperature in Celsius", example = "2.8")
    private Double windchillC;

    @Schema(description = "Heat index in Celsius", example = "6.7")
    private Double heatindexC;

    @Schema(description = "Dew point in Celsius", example = "-1.5")
    private Double dewpointC;

    @Schema(description = "Visibility in kilometers", example = "10")
    private Double visKm;

    @Schema(description = "UV index", example = "2.7")
    private Double uv;

    @Schema(description = "Wind gust in kilometers per hour", example = "27.3")
    private Double gustKph;
}
