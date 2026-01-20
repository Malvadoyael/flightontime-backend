package com.flightontime.backend.service.genai;

import com.google.genai.types.HttpOptions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class GenAiLibraryInspectorTest {

    @Test
    public void printHttpOptionsMethods() {
        System.out.println("--- Inspecting HttpOptions.Builder methods ---");
        Method[] methods = HttpOptions.Builder.class.getMethods();
        for (Method m : methods) {
            if (m.getName().equals("timeout")) {
                System.out.println("Found timeout method: " + m);
                System.out.println("Parameter types: " + Arrays.toString(m.getParameterTypes()));
            }
        }
        System.out.println("--- End Inspection ---");
    }
}
