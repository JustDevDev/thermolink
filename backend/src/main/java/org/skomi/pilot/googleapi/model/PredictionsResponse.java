package org.skomi.pilot.googleapi.model;

import lombok.Data;

import java.util.List;

@Data
public class PredictionsResponse {
    private List<Prediction> predictions;
}
