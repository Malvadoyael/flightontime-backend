package com.flightontime.backend.controller;

import com.flightontime.backend.model.Airline;
import com.flightontime.backend.model.Airport;
import com.flightontime.backend.model.Flight;
import com.flightontime.backend.repository.AirlineRepository;
import com.flightontime.backend.service.FlightService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class StatusController {

    /**
     * Controlador REST para endpoints de predicción y lista de orígenes.
     */

    @Autowired
    private FlightService flightService;
    @Autowired
    private AirlineRepository airlineRepository;
    @Autowired
    private com.flightontime.backend.repository.AirportRepository airportRepository;
    @Autowired
    private com.flightontime.backend.repository.FlightMatchRepository flightMatchRepository;

    // Cambiamos a POST para que TÚ le envíes los datos del vuelo
    @PostMapping("/predict")
    /**
     * Recibe un `Flight` en el cuerpo de la petición y devuelve el mismo objeto
     * con la predicción de probabilidad de retraso calculada por
     * {@link com.flightontime.backend.service.FlightService#predictDelay(Flight)}.
     *
     * @param flight objeto `Flight` con datos de entrada
     * @return objeto `Flight` con la propiedad `delayProbability` calculada
     */
    public Flight getPrediction(@RequestBody Flight flight) {
        return flightService.predictDelay(flight);
    }

    @PostMapping("/originList")
    /**
     * Devuelve una lista de vuelos de ejemplo que representan orígenes
     * disponibles.
     *
     * @return lista de `Flight` de ejemplo
     */
    public List<Flight> originList() {
        return flightService.originList();
    }

    @PostMapping("/test-model")
    /**
     * Endpoint para probar la integración con el modelo XGBoost.
     * Rellena datos del vuelo basado en features del modelo.
     */
    public Flight testModel(@RequestBody Flight flight) {
        return flightService.testingModelEdu(flight);
    }

    @PostMapping("/validate-terna")
    /**
     * Endpoint para validar si el modelo soporta la terna de IDs (Origen, Destino,
     * Aerolinea).
     * Se espera un objeto Flight con ids dummy para mapear a los argumentos, o un
     * objeto custom.
     * Para simplificar, usaremos parametros de request o un objeto wrapper.
     * Aquí usaremos un objeto wrapper interno o map para recibir los IDs.
     */
    public boolean validateTerna(@RequestBody TernaRequest request) {
        return flightService.validateTernaIds(request.originId, request.destId, request.airlineId);
    }

    // Clase auxiliar para el request bodt
    public static class TernaRequest {
        public int originId;
        public int destId;
        public int airlineId;
    }

    @PostMapping("/get-airline")
    public List<Airline> getAirline(@RequestBody AirlineRequest request) {
        return airlineRepository.findByActive(Boolean.parseBoolean(request.getActive()));
    }

    @PostMapping("/get-airport")
    public List<Airport> getAirport() {
        return airportRepository.findAll();
    }

    @PostMapping("/get-destinoById")
    public List<com.flightontime.backend.model.AirlineOriginProjection> getDestinationsByAirline(
            @RequestBody DestinationRequest request) {
        return flightMatchRepository.findDestinationsByAirlineId(request.getAirlineId());
    }

    public static class DestinationRequest {
        private Integer airlineId;

        public Integer getAirlineId() {
            return airlineId;
        }

        public void setAirlineId(Integer airlineId) {
            this.airlineId = airlineId;
        }
    }

    @Autowired
    private com.flightontime.backend.service.OnnxPredictionService onnxPredictionService;

    @Autowired
    private com.flightontime.backend.service.weather.WeatherService weatherService;

    @PostMapping("/predict-smart")
    public Flight predictSmart(@RequestBody FlightWeatherRequest request) {
        try {
            // 1. Get origin and destination airport coordinates
            Airport originAirport = airportRepository.findById(request.getFlight().getOrigin().longValue())
                    .orElse(null);
            Airport destAirport = airportRepository.findById(request.getFlight().getDestination().longValue())
                    .orElse(null);

            Double tempMax = 20.0;
            Double rainSum = 0.0;
            Double snowSum = 0.0;
            Double windSpeed = 10.0;
            int weatherCode = 0;
            double distance = flightService.calculateDistance(originAirport, destAirport);

            // 2. Fetch real weather if airport found and date is present
            if (originAirport != null && request.getFlight().getDepartureTime() != null) {
                com.flightontime.backend.model.weather.WeatherRequest wr = new com.flightontime.backend.model.weather.WeatherRequest();
                wr.setLatitude(String.valueOf(originAirport.getLatitude()));
                wr.setLongitude(String.valueOf(originAirport.getLongitude()));
                wr.setFechaVuelo(java.sql.Date.valueOf(request.getFlight().getDepartureTime().toLocalDate()));

                com.flightontime.backend.model.weather.WeatherResponse wResponse = weatherService.processWeather(wr);

                if (wResponse != null && wResponse.getHourly() != null) {
                    // Extract data for the specific flight hour
                    int flightHour = request.getFlight().getDepartureTime().getHour();

                    // The hourly list usually starts at 00:00 of start_date (yesterday in our
                    // logic) or today.
                    // Our logic in WeatherService requests start_date = flightDate - 1 day.
                    // So index 0 is yesterday 00:00.
                    // Index 24 is today 00:00.
                    // Index 24 + flightHour is our target.

                    int index = 24 + flightHour;

                    // Safety check boundaries
                    List<Double> temps = wResponse.getHourly().getTemperature2m();
                    if (temps != null && index < temps.size()) {
                        // We need MAX temp for the day, not just hourly.
                        // Let's approximate max temp from the 24h block of "today" (index 24 to 47)
                        tempMax = -100.0;
                        for (int k = 24; k < 48 && k < temps.size(); k++) {
                            if (temps.get(k) > tempMax)
                                tempMax = temps.get(k);
                        }
                    }

                    List<Double> rains = wResponse.getHourly().getPrecipitation();
                    if (rains != null && index < rains.size()) {
                        // Rain sum for the moment or day? Model says "rain_sum".
                        // Usually accumulated. Let's sum up rain for the day (24-47)
                        for (int k = 24; k < 48 && k < rains.size(); k++) {
                            rainSum += rains.get(k);
                        }
                    }

                    List<Double> snows = wResponse.getHourly().getSnowDepth(); // Or snowfall?
                    // ONNX feature is "snow_sum". Open-Meteo provides snowfall or snow_depth.
                    // Let's check if we have snowfall. Hourly class has snowDepth.
                    // Let's use snowDepth or 0.
                    if (snows != null && index < snows.size()) {
                        snowSum = snows.get(index); // Depth at that specific hour
                    }

                    List<Double> winds = wResponse.getHourly().getWindSpeed10m();
                    if (winds != null && index < winds.size()) {
                        windSpeed = winds.get(index);
                    }

                    List<Integer> codes = wResponse.getHourly().getWeatherCode();
                    if (codes != null && index < codes.size()) {
                        weatherCode = codes.get(index);
                    }
                }
            }

            // 3. Predict
            double prob = onnxPredictionService.predict(
                    request.getFlight(),
                    tempMax,
                    rainSum,
                    snowSum,
                    windSpeed,
                    weatherCode,
                    distance, // Nueva distancia calculada
                    request.getDayOfWeek(),
                    request.getHour(),
                    request.getMinute());
            request.getFlight().setDelayProbability(prob);
            request.getFlight().setDistance(distance);
            return request.getFlight();
        } catch (Exception e) {
            e.printStackTrace();
            return request.getFlight(); // Return original on error
        }
    }

    @PostMapping("/predict-onnx")
    public Flight predictOnnx(@RequestBody FlightWeatherRequest request) {
        try {
            // Also calculate distance here for consistency
            Airport originAirport = airportRepository.findById(request.getFlight().getOrigin().longValue())
                    .orElse(null);
            Airport destAirport = airportRepository.findById(request.getFlight().getDestination().longValue())
                    .orElse(null);
            double distance = flightService.calculateDistance(originAirport, destAirport);

            double prob = onnxPredictionService.predict(
                    request.getFlight(),
                    request.getTempMax() != null ? request.getTempMax() : 20.0,
                    request.getRainSum() != null ? request.getRainSum() : 0.0,
                    request.getSnowSum() != null ? request.getSnowSum() : 0.0,
                    request.getWindSpeed() != null ? request.getWindSpeed() : 10.0,
                    request.getWeatherCode() != null ? request.getWeatherCode() : 0,
                    distance,
                    request.getDayOfWeek(),
                    request.getHour(),
                    request.getMinute());
            request.getFlight().setDelayProbability(prob);
            request.getFlight().setDistance(distance);
            return request.getFlight();
        } catch (Exception e) {
            e.printStackTrace();
            return request.getFlight(); // Return original on error
        }
    }

    public static class FlightWeatherRequest {
        private Flight flight;
        private Double tempMax;
        private Double rainSum;
        private Double snowSum;
        private Double windSpeed;
        private Integer weatherCode;
        // Loose time parameters
        private Integer month; // Requested by user (unused by current model but allowed)
        private Integer dayOfWeek;
        private Integer hour;
        private Integer minute;

        public Flight getFlight() {
            return flight;
        }

        public void setFlight(Flight flight) {
            this.flight = flight;
        }

        public Double getTempMax() {
            return tempMax;
        }

        public void setTempMax(Double tempMax) {
            this.tempMax = tempMax;
        }

        public Double getRainSum() {
            return rainSum;
        }

        public void setRainSum(Double rainSum) {
            this.rainSum = rainSum;
        }

        public Double getSnowSum() {
            return snowSum;
        }

        public void setSnowSum(Double snowSum) {
            this.snowSum = snowSum;
        }

        public Double getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(Double windSpeed) {
            this.windSpeed = windSpeed;
        }

        public Integer getWeatherCode() {
            return weatherCode;
        }

        public void setWeatherCode(Integer weatherCode) {
            this.weatherCode = weatherCode;
        }

        public Integer getMonth() {
            return month;
        }

        public void setMonth(Integer month) {
            this.month = month;
        }

        public Integer getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(Integer dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public Integer getHour() {
            return hour;
        }

        public void setHour(Integer hour) {
            this.hour = hour;
        }

        public Integer getMinute() {
            return minute;
        }

        public void setMinute(Integer minute) {
            this.minute = minute;
        }
    }
}