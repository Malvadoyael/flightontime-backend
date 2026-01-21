# ✈️ FlightOnTime - Backend API

Este repositorio contiene el servicio Backend para la aplicación **FlightOnTime**. Está construido con **Java Spring Boot** y gestiona la lógica de predicción de vuelos, geolocalización de aeropuertos y rutas aéreas.

El sistema incluye una base de datos en memoria (H2) que se autopuebla al iniciar con más de **300 aeropuertos**, **15 aerolíneas** y **12,000+ rutas de vuelo** validadas.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17 (o la versión que uses, ej. 21)
* **Framework:** Spring Boot 3.x
* **Base de Datos:** H2 Database (En memoria)
* **Gestor de Dependencias:** Maven
* **Persistencia:** JPA / Hibernate

---

## 🚀 Instrucciones de Instalación y Ejecución

Sigue estos pasos para levantar el servidor en tu máquina local.

### 1. Prerrequisitos
* Tener instalado Java JDK (versión 17 o superior).
* Tener Git instalado.

### 2. Clonar el repositorio
```bash
git clone [https://github.com/Malvadoyael/flightontime-backend.git](https://github.com/Malvadoyael/flightontime-backend.git)
cd flightontime-backend
