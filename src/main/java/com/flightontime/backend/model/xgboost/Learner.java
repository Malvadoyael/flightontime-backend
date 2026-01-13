package com.flightontime.backend.model.xgboost;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class Learner {
    @JsonProperty("feature_names")
    private List<String> featureNames;

    @JsonProperty("feature_types")
    private List<String> featureTypes;

    @JsonProperty("gradient_booster")
    private GradientBooster gradientBooster;
}
