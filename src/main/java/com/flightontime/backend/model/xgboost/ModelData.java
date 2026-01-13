package com.flightontime.backend.model.xgboost;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ModelData {
    private List<Tree> trees;

    @JsonProperty("gbtree_model_param")
    private Map<String, String> gbtreeModelParam;
}
