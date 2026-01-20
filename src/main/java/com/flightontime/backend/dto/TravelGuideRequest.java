package com.flightontime.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TravelGuideRequest {

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("longitude")
    private String longitude;

    @JsonProperty("travelDate")
    private String travelDate;

    // Default constructor
    public TravelGuideRequest() {
    }

    public TravelGuideRequest(String latitude, String longitude, String travelDate) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.travelDate = travelDate;
    }
}
