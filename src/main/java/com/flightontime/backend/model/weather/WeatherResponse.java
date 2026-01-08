package com.flightontime.backend.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class WeatherResponse {

    private Double latitude;
    private Double longitude;
    @JsonProperty("generationtime_ms")
    private Double generationtimeMs;
    @JsonProperty("utc_offset_seconds")
    private Integer utcOffsetSeconds;
    private String timezone;
    @JsonProperty("timezone_abbreviation")
    private String timezoneAbbreviation;
    private Double elevation;
    @JsonProperty("hourly_units")
    private HourlyUnits hourlyUnits;
    private Hourly hourly;

    @Data
    public static class HourlyUnits {
        private String time;
        @JsonProperty("temperature_2m")
        private String temperature2m;
        @JsonProperty("dew_point_2m")
        private String dewPoint2m;
        private String precipitation;
        @JsonProperty("weather_code")
        private String weatherCode;
        private String visibility;
        @JsonProperty("wind_speed_10m")
        private String windSpeed10m;
        @JsonProperty("wind_direction_10m")
        private String windDirection10m;
        @JsonProperty("wind_gusts_10m")
        private String windGusts10m;
        @JsonProperty("freezing_level_height")
        private String freezingLevelHeight;
        @JsonProperty("cloud_cover_low")
        private String cloudCoverLow;
        @JsonProperty("snow_depth")
        private String snowDepth;
        private String cape;
    }

    @Data
    public static class Hourly {
        private List<String> time;
        @JsonProperty("temperature_2m")
        private List<Double> temperature2m;
        @JsonProperty("dew_point_2m")
        private List<Double> dewPoint2m;
        private List<Double> precipitation;
        @JsonProperty("weather_code")
        private List<Integer> weatherCode;
        private List<Double> visibility;
        @JsonProperty("wind_speed_10m")
        private List<Double> windSpeed10m;
        @JsonProperty("wind_direction_10m")
        private List<Integer> windDirection10m;
        @JsonProperty("wind_gusts_10m")
        private List<Double> windGusts10m;
        @JsonProperty("freezing_level_height")
        private List<Double> freezingLevelHeight;
        @JsonProperty("cloud_cover_low")
        private List<Integer> cloudCoverLow;
        @JsonProperty("snow_depth")
        private List<Double> snowDepth;
        private List<Double> cape;
    }
}
