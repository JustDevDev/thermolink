package org.skomi.pilot.ui.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlcPort {
    @JsonProperty("PLCId")
    String PLCId;
    Integer port;
}
