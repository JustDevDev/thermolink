package org.skomi.pilot.weatherapi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Location information")
public class LocationDto {
    @Schema(description = "Name of the city")
    private String name;
    
    @Schema(description = "Region/state name")
    private String region;
    
    @Schema(description = "Country name")
    private String country;
    
    @Schema(description = "Latitude coordinate")
    private Double lat;
    
    @Schema(description = "Longitude coordinate")
    private Double lon;

    @JsonAlias("tz_id")
    @Schema(description = "Timezone identifier")
    private String tzId;
    
    @Schema(description = "Local time in epoch format")
    private Long localtimeEpoch;
    
    @Schema(description = "Local time in readable format", example = "2025-03-15 13:05")
    private String localtime;
}
