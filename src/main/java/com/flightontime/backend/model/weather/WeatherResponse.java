package com.flightontime.backend.model.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getGenerationtimeMs() {
        return generationtimeMs;
    }

    public void setGenerationtimeMs(Double generationtimeMs) {
        this.generationtimeMs = generationtimeMs;
    }

    public Integer getUtcOffsetSeconds() {
        return utcOffsetSeconds;
    }

    public void setUtcOffsetSeconds(Integer utcOffsetSeconds) {
        this.utcOffsetSeconds = utcOffsetSeconds;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezoneAbbreviation() {
        return timezoneAbbreviation;
    }

    public void setTimezoneAbbreviation(String timezoneAbbreviation) {
        this.timezoneAbbreviation = timezoneAbbreviation;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public HourlyUnits getHourlyUnits() {
        return hourlyUnits;
    }

    public void setHourlyUnits(HourlyUnits hourlyUnits) {
        this.hourlyUnits = hourlyUnits;
    }

    public Hourly getHourly() {
        return hourly;
    }

    public void setHourly(Hourly hourly) {
        this.hourly = hourly;
    }

    public static class HourlyUnits {
        private String time;
        @JsonProperty("temperature_2m")
        private String temperature2m;
        private String visibility;
        @JsonProperty("wind_speed_10m")
        private String windSpeed10m;
        @JsonProperty("wind_direction_10m")
        private String windDirection10m;
        @JsonProperty("wind_gusts_10m")
        private String windGusts10m;
        @JsonProperty("cloud_cover")
        private String cloudCover;
        @JsonProperty("weather_code")
        private String weatherCode;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTemperature2m() {
            return temperature2m;
        }

        public void setTemperature2m(String temperature2m) {
            this.temperature2m = temperature2m;
        }

        public String getVisibility() {
            return visibility;
        }

        public void setVisibility(String visibility) {
            this.visibility = visibility;
        }

        public String getWindSpeed10m() {
            return windSpeed10m;
        }

        public void setWindSpeed10m(String windSpeed10m) {
            this.windSpeed10m = windSpeed10m;
        }

        public String getWindDirection10m() {
            return windDirection10m;
        }

        public void setWindDirection10m(String windDirection10m) {
            this.windDirection10m = windDirection10m;
        }

        public String getWindGusts10m() {
            return windGusts10m;
        }

        public void setWindGusts10m(String windGusts10m) {
            this.windGusts10m = windGusts10m;
        }

        public String getCloudCover() {
            return cloudCover;
        }

        public void setCloudCover(String cloudCover) {
            this.cloudCover = cloudCover;
        }

        public String getWeatherCode() {
            return weatherCode;
        }

        public void setWeatherCode(String weatherCode) {
            this.weatherCode = weatherCode;
        }
    }

    public static class Hourly {
        private List<String> time;
        @JsonProperty("temperature_2m")
        private List<Double> temperature2m;
        private List<Integer> visibility;
        @JsonProperty("wind_speed_10m")
        private List<Double> windSpeed10m;
        @JsonProperty("wind_direction_10m")
        private List<Integer> windDirection10m;
        @JsonProperty("wind_gusts_10m")
        private List<Double> windGusts10m;
        @JsonProperty("cloud_cover")
        private List<Integer> cloudCover;
        @JsonProperty("weather_code")
        private List<Integer> weatherCode;

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public List<Double> getTemperature2m() {
            return temperature2m;
        }

        public void setTemperature2m(List<Double> temperature2m) {
            this.temperature2m = temperature2m;
        }

        public List<Integer> getVisibility() {
            return visibility;
        }

        public void setVisibility(List<Integer> visibility) {
            this.visibility = visibility;
        }

        public List<Double> getWindSpeed10m() {
            return windSpeed10m;
        }

        public void setWindSpeed10m(List<Double> windSpeed10m) {
            this.windSpeed10m = windSpeed10m;
        }

        public List<Integer> getWindDirection10m() {
            return windDirection10m;
        }

        public void setWindDirection10m(List<Integer> windDirection10m) {
            this.windDirection10m = windDirection10m;
        }

        public List<Double> getWindGusts10m() {
            return windGusts10m;
        }

        public void setWindGusts10m(List<Double> windGusts10m) {
            this.windGusts10m = windGusts10m;
        }

        public List<Integer> getCloudCover() {
            return cloudCover;
        }

        public void setCloudCover(List<Integer> cloudCover) {
            this.cloudCover = cloudCover;
        }

        public List<Integer> getWeatherCode() {
            return weatherCode;
        }

        public void setWeatherCode(List<Integer> weatherCode) {
            this.weatherCode = weatherCode;
        }
    }
}
