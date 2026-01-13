package com.flightontime.backend.model.xgboost;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class Tree {
    private Integer id;

    @JsonProperty("tree_info")
    private List<Integer> treeInfo;

    @JsonProperty("base_weights")
    private List<Double> baseWeights;

    @JsonProperty("left_children")
    private List<Integer> leftChildren;

    @JsonProperty("right_children")
    private List<Integer> rightChildren;

    @JsonProperty("split_conditions")
    private List<Double> splitConditions;

    @JsonProperty("split_indices")
    private List<Integer> splitIndices;

    @JsonProperty("parents")
    private List<Integer> parents;
}
