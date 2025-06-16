package org.skomi.pilot.googleapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class StructuredFormatting {
    @JsonAlias("main_text")
    private String mainText;
}