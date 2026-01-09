package com.flightontime.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

/**
 * Configuración global de la aplicación web.
 * <p>
 * Esta clase implementa WebMvcConfigurer para definir la configuración de
 * Cross-Origin Resource Sharing (CORS).
 * Permite que clientes frontend (como React, Angular o Vue ejecutándose en
 * puertos locales)
 * realicen peticiones HTTP a este backend sin ser bloqueados por el navegador.
 * </p>
 * <p>
 * <strong>Para entornos de Producción:</strong>
 * Si despliegas esta aplicación en un servidor remoto, debes agregar la URL
 * de tu frontend en el método {@code allowedOrigins}.
 * <br>
 * Ejemplo:
 * 
 * <pre>
 * .allowedOrigins(
 *     "https://mi-dominio-produccion.com",
 *     "https://otra-url-permitida.com"
 * )
 * </pre>
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura las rutas CORS permitidas para la aplicación.
     *
     * @param registry registro de CORS proporcionado por Spring MVC
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all endpoints
                .allowedOrigins(
                        "http://localhost:5173", // Vite/React default
                        "http://localhost:8080" // Self
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
