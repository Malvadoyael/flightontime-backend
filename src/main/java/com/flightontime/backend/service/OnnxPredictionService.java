package com.flightontime.backend.service;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightontime.backend.model.Flight;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

import java.util.*;

@Service
public class OnnxPredictionService {

    private OrtEnvironment env;
    private OrtSession session;
    private List<String> featureNames;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() throws IOException, OrtException {
        // 1. Initialize ONNX Environment
        this.env = OrtEnvironment.getEnvironment();

        // 2. Load the Model
        // Note: loading from classpath resource to a temp file might be needed if
        // standard load fails,
        // but try direct path if possible or stream.
        // ONNX Runtime usually needs a file path.
        ClassPathResource modelResource = new ClassPathResource("modelo_retrasos_light.onnx");
        // Convert to temp file because ORT needs a path
        java.io.File modelFile = java.io.File.createTempFile("model", ".onnx");
        try (InputStream is = modelResource.getInputStream()) {
            java.nio.file.Files.copy(is, modelFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        this.session = env.createSession(modelFile.getAbsolutePath(), new OrtSession.SessionOptions());

        // 3. Load Feature Names
        ClassPathResource jsonResource = new ClassPathResource("json_con_clima.json");
        this.featureNames = objectMapper.readValue(jsonResource.getInputStream(), List.class);

        System.out.println("ONNX Model loaded. Feature count: " + featureNames.size());
    }

    public double predict(Flight flight, double tempMax, double rainSum, double snowSum, double windSpeed,
            int weatherCode, double distance, Integer dayOfWeek, Integer hour, Integer minute) throws OrtException {
        // 1. Prepare Input Float Array
        float[] inputData = new float[featureNames.size()];
        Arrays.fill(inputData, 0.0f); // Initialize with 0

        // Defaults if null
        int dOfW = dayOfWeek != null ? dayOfWeek
                : (flight.getDepartureTime() != null ? flight.getDepartureTime().getDayOfWeek().getValue() : 1);
        int h = hour != null ? hour : (flight.getDepartureTime() != null ? flight.getDepartureTime().getHour() : 12);
        int m = minute != null ? minute
                : (flight.getDepartureTime() != null ? flight.getDepartureTime().getMinute() : 0);

        // 2. Fill Features
        for (int i = 0; i < featureNames.size(); i++) {
            String feature = featureNames.get(i);

            // -- Numerical Features --
            if (feature.equals("day_of_week")) {
                inputData[i] = (float) dOfW;
            } else if (feature.equals("op_carrier_fl_num")) {
                try {
                    inputData[i] = Float.parseFloat(flight.getFlightNumber());
                } catch (NumberFormatException | NullPointerException e) {
                    inputData[i] = 0; // Fallback
                }
            } else if (feature.equals("crs_dep_time")) {
                inputData[i] = h * 100 + m;
            } else if (feature.equals("crs_arr_time")) {
                // FIXME: We don't have arrival time in Flight object yet. Estimating +2 hours.
                int arrH = h + 2;
                if (arrH > 23)
                    arrH -= 24;
                inputData[i] = arrH * 100 + m;
            } else if (feature.equals("distance")) {
                inputData[i] = (float) distance;
            } else if (feature.equals("temp_max")) {
                inputData[i] = (float) tempMax;
            } else if (feature.equals("rain_sum")) {
                inputData[i] = (float) rainSum;
            } else if (feature.equals("snow_sum")) {
                inputData[i] = (float) snowSum;
            } else if (feature.equals("wind_speed")) {
                inputData[i] = (float) windSpeed;
            } else if (feature.equals("weather_code")) {
                inputData[i] = (float) weatherCode;
            }

            // -- One Hot Encoding --
            // Format: origin_123, dest_456, op_unique_carrier_789

            else if (feature.startsWith("origin_")) {
                String idStr = feature.replace("origin_", "");
                if (flight.getOrigin() != null && String.valueOf(flight.getOrigin()).equals(idStr)) {
                    inputData[i] = 1.0f;
                }
            } else if (feature.startsWith("dest_")) {
                String idStr = feature.replace("dest_", "");
                if (flight.getDestination() != null && String.valueOf(flight.getDestination()).equals(idStr)) {
                    inputData[i] = 1.0f;
                }
            } else if (feature.startsWith("op_unique_carrier_")) {
                String idStr = feature.replace("op_unique_carrier_", "");
                if (flight.getAirline() != null && String.valueOf(flight.getAirline()).equals(idStr)) {
                    inputData[i] = 1.0f;
                }
            }
        }

        // 3. Create Tensor
        // Shape: [1, n_features]
        long[] shape = new long[] { 1, featureNames.size() };
        OnnxTensor tensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(inputData), shape);

        // 4. Run Inference
        // Input name needs to be correct. Usually "float_input" or similar.
        // We can get it from session.getInputNames() but hard to do strictly inside
        // init.
        // We'll iterate the input names.
        String inputName = session.getInputNames().iterator().next();

        OrtSession.Result result = session.run(Collections.singletonMap(inputName, tensor));

        // 5. Extract Result
        // Output is usually "label" (int) and "probabilities" (map or list).
        // Let's print output names to debug if needed.
        // Assuming output 1 is probabilities.

        // Typical Sklearn/XGBoost ONNX output:
        // 0: label (int64)
        // 1: probabilities (sequence<map<int64, float>>)

        Optional<OnnxTensor> probTensorOpt = Optional.empty();

        // Try getting the second output if it exists (probs)
        Iterator<String> outputNames = session.getOutputNames().iterator();
        String labelName = outputNames.next();
        String probName = outputNames.hasNext() ? outputNames.next() : null;

        if (probName != null) {
            // This part depends heavily on how the ONNX model was exported.
            // Often it returns a List of Maps.
            // Since dealing with Java maps from ONNX is tricky, let's look at the result
            // object.
            Object value = result.get(probName).get().getValue();
            // In Java ONNX Runtime, a sequence of maps might be handled differently.
            // Simplified: let's try to just return the label for now, or try to get output
            // 0 if
            // it's regression.

            // If it returns a simplified tensor float[1][2] (prob class 0, prob class 1)
            if (value instanceof float[][]) {
                float[][] probs = (float[][]) value;
                return probs[0][1]; // Probability of class 1 (Delay)
            }

            // If it's a list of maps, we might need more complex parsing.
            // For safety, let's return 0.5 if we can't parse, or try to get output 0 if
            // it's regression.

            System.out.println("Output type: " + value.getClass().getName());
        }

        // Fallback: return label (0 or 1) as double
        Object labelVal = result.get(labelName).get().getValue();
        if (labelVal instanceof long[]) {
            long[] labels = (long[]) labelVal;
            return (double) labels[0];
        }

        return -1.0;
    }
}
