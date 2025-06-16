package org.skomi.pilot.googleapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * Represents a prediction result returned by autocomplete API.
 */
@Schema(description = "Represents a prediction result, typically returned by a place or autocomplete API.")
@Data
public class Prediction {

    @Schema(description = "The description or name of the place.")
    private String description;

    @JsonAlias("place_id")
    @Schema(description = "The unique identifier of the place.")
    private String placeId;

    @Schema(description = "A reference token for additional details or lookup of the place.")
    private String reference;

    @JsonAlias("structured_formatting")
    @Schema(description = "Structured information for formatting and display of the place details.")
    private StructuredFormatting structuredFormatting;
}
