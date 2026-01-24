"""
Script para analizar rutas de vuelos y identificar rutas con múltiples aerolíneas.

Este script lee un archivo JSON con datos de retrasos de vuelos, agrupa las rutas por origen-destino
y aerolíneas, e identifica las rutas que tienen más de una aerolínea operando.
"""

import json
import sys

# Ruta al archivo JSON con datos de retrasos
file_path = r'c:\Users\Eduardo1\Downloads\flightontime-backend\src\main\resources\retrasos_lista_plana.json'

try:
    with open(file_path, 'r', encoding='utf-8') as f:
        data = json.load(f)

    routes = {}

    for entry in data:
        od = entry.get('origen_destino')
        airline = entry.get('aerolinea')

        if od and airline:
            if od not in routes:
                routes[od] = set()
            routes[od].add(airline)

    multi_airline_routes = {k: v for k, v in routes.items() if len(v) > 1}

    if not multi_airline_routes:
        print("No routes found with multiple airlines.")
    else:
        print(f"Found {len(multi_airline_routes)} routes with multiple airlines:")
        for route, airlines in multi_airline_routes.items():
            print(f"Route: {route}, Airlines: {', '.join(sorted(list(airlines)))}")

except FileNotFoundError:
    print(f"Error: File not found at {file_path}")
except json.JSONDecodeError:
    print("Error: Failed to decode JSON")
except Exception as e:
    print(f"An error occurred: {e}")
