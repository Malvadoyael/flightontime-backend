# ✈️ FlightOnTime - Backend API

Este repositorio contiene el servicio Backend para la aplicación **FlightOnTime**. Está construido con **Java Spring Boot 3.x** y gestiona la lógica de predicción de vuelos, geolocalización de aeropuertos y guías de viaje inteligentes.

El sistema incluye una base de datos en memoria (**H2**) que se autopuebla al iniciar con más de **300 aeropuertos**, **15 aerolíneas** y **12,000+ rutas de vuelo** validadas.

---

## 🛠️ Tecnologías Utilizadas

*   **Lenguaje:** Java 17+
*   **Framework:** Spring Boot 3.5.9
*   **Base de Datos:** H2 Database (En memoria)
*   **IA & Machine Learning:**
    *   **Google Gemini (GenAI):** Para generación de contenido y guías de viaje.
    *   **ONNX Runtime:** Para predicción de retrasos basada en modelos de ML.
*   **Documentación API:** SpringDoc OpenAPI (Swagger UI).
*   **Herramientas:** Maven (Build), Lombok (Boilerplate reduction), Jackson (JSON).

---

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas clásica, diseñada para ser modular, escalable y fácil de mantener.

### Estructura de Paquetes (`src/main/java/com/flightontime/backend`)

*   **`controller`**: Capa de presentación (REST API). Define los endpoints, maneja las solicitudes HTTP y delega la ejecución a la capa de servicio.
*   **`service`**: Capa de lógica de negocio. Contiene las reglas del negocio, orquesta llamadas a repositorios y servicios externos (como la API de IA).
*   **`repository`**: Capa de acceso a datos. Interfaces que extienden `JpaRepository` para interactuar con la base de datos H2.
*   **`model`**: Entidades JPA que representan las tablas de la base de datos (Ej: `Airport`, `Airline`, `Flight`).
*   **`dto` (Data Transfer Objects)**: Objetos simples utilizados para transportar datos entre procesos y definir la estructura de las respuestas JSON, desacoplando la API del modelo de datos interno.
*   **`config`**: Configuraciones generales de la aplicación (Beans, CORS, Clientes HTTP).

---

## 💡 Mejores Prácticas Implementadas

Este proyecto sirve como ejemplo de varias mejores prácticas en el desarrollo con Spring Boot:

1.  **Separación de Responsabilidades (SoC)**: Cada capa tiene una responsabilidad única y clara. Los controladores no acceden a la base de datos directamente; los servicios no manejan respuestas HTTP crudas.
2.  **Uso de DTOs**: Se utilizan DTOs (como `TravelGuideRequest`, `FlightPredictionDto`) para definir contratos de API estrictos y evitar exponer detalles internos de las entidades de la base de datos.
3.  **Inyección de Dependencias**: Se utiliza el contenedor IoC de Spring para gestionar las dependencias, facilitando el testing unitario y la modularidad.
4.  **Código Limpio con Lombok**: Uso de anotaciones como `@Data`, `@Builder`, y `@RequiredArgsConstructor` para minimizar el código repetitivo (getters, setters, constructores).
5.  **Documentación Automática**: Integración con Swagger UI para que la documentación de la API evolucione automáticamente junto con el código.

---

## 🔒 Configuración y Seguridad

### Variables de Entorno y Claves API

> [!IMPORTANT]
> **Nota de Seguridad**: Actualmente, el archivo `application.properties` puede contener la clave `gemini.api.key` hardcodeada para facilitar la ejecución local.

**Mejor Práctica Recomendada**: En un entorno productivo o compartido, **nunca** se deben incluir credenciales en el código fuente. Se recomienda usar variables de entorno:

1.  En tu sistema operativo, define la variable: `export GEMINI_API_KEY=tu_clave_real`
2.  En `application.properties`, referencia la variable:
    ```properties
    gemini.api.key=${GEMINI_API_KEY}
    ```

---

## 🚀 Instrucciones de Instalación y Ejecución

### 1. Prerrequisitos
*   Java JDK 17 o superior.
*   Git.

### 2. Clonar y Construir
```bash
git clone https://github.com/Malvadoyael/flightontime-backend.git
cd flightontime-backend
./mvnw clean install
```

### 3. Ejecutar
```bash
./mvnw spring-boot:run
```
El servidor iniciará en `http://localhost:8080`.

---

## 📡 API Endpoints Principales

Accede a la documentación interactiva completa en:  
👉 **`http://localhost:8080/swagger-ui.html`**

### 📊 Dashboard
*   `GET /api/v1/dashboard/delays-by-month`: Resumen mensual de retrasos.

### 🤖 Predicciones e IA
*   `POST /api/v1/predict-probability`: Calcula la probabilidad de retraso de un vuelo usando el modelo ONNX.
*   `POST /api/v1/travel-guide`: Genera una guía de viaje personalizada usando Google Gemini.

### 🌍 Datos Maestros
*   `GET /api/v1/airports`: Lista de aeropuertos soportados.
*   `GET /api/v1/status`: Verificación de estado del sistema (Health Check).

---

## 🧪 Pruebas

Ejecuta la suite de pruebas automatizadas:
```bash
./mvnw test
```

---

## 🤝 Contribución

1.  Crea un Fork del repositorio.
2.  Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`).
3.  Envía un Pull Request detallando tus cambios.

---

## 🏆 Contexto y Equipo

Este proyecto nació como parte de la **Hackathon de NoCountry y Oracle Next Education**.

### 👥 Equipo
*   **Arquitecto**: **@educhile1** - Definición de la arquitectura, diseño de soluciones y participación activa en la generación de código y estrategia técnica, desarrollo de la API de integración, integraciones con IA.
*   **Desarrollador**: **Malvadoyael** - Desarrollo y mejoras continuas de la aplicación.

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT.
