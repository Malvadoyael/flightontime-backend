"""
Script para extraer mapeos de aeropuertos y aerolíneas desde archivos SQL y JSON.

Este script analiza un archivo SQL para extraer datos de aeropuertos y aerolíneas,
y un archivo JSON para obtener IDs de características de rutas y aerolíneas.
"""

import json
import re

def parse_sql_data(file_path):
    """
    Parsea un archivo SQL para extraer datos de aeropuertos y aerolíneas.

    Args:
        file_path (str): Ruta al archivo SQL.

    Returns:
        tuple: Diccionarios de aeropuertos y aerolíneas mapeados por ID.
    """
    airports = {}
    airlines = {}
    
    airport_id_counter = 1
    airline_id_counter = 1
    
    with open(file_path, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.strip()
            # Parse Airport
            if line.startswith("INSERT INTO AIRPORT"):
                # Exact format: VALUES ('ABE', 'Lehigh Valley International', 'Allentown', 'PA', 40.65, -75.44);
                # Regex to capture content inside VALUES (...)
                match = re.search(r"VALUES \('([^']*)', '([^']*)', '([^']*)', '([^']*)'", line)
                if match:
                    iata, name, city, state = match.groups()
                    airports[str(airport_id_counter)] = f"{iata} - {name} ({city}, {state})"
                    airport_id_counter += 1
            
            # Parse Airline
            elif line.startswith("INSERT INTO AIRLINE"):
                # VALUES ('9E', 'Endeavor Air', true);
                match = re.search(r"VALUES \('([^']*)', '([^']*)'", line)
                if match:
                    code, name = match.groups()
                    airlines[str(airline_id_counter)] = f"{code} - {name}"
                    airline_id_counter += 1
                    
    return airports, airlines

def parse_json_features(file_path):
    """
    Parsea un archivo JSON para extraer IDs de características de rutas.

    Args:
        file_path (str): Ruta al archivo JSON.

    Returns:
        tuple: Conjuntos de IDs de origen, destino y aerolíneas.
    """
    with open(file_path, 'r', encoding='utf-8') as f:
        features = json.load(f)
        
    origin_ids = set()
    dest_ids = set()
    airline_ids = set()
    
    for feat in features:
        if feat.startswith("origin_"):
            origin_ids.add(feat.replace("origin_", ""))
        elif feat.startswith("dest_"):
            dest_ids.add(feat.replace("dest_", ""))
        elif feat.startswith("op_unique_carrier_"):
            airline_ids.add(feat.replace("op_unique_carrier_", ""))
            
    return origin_ids, dest_ids, airline_ids


def main():
    airports, airlines = parse_sql_data("src/main/resources/data.sql")
    origin_ids, dest_ids, airline_ids = parse_json_features("src/main/resources/json_con_clima.json")
    
    print("### Aerolíneas Soportadas (Available to use in 'airline' field)")
    print("| ID | Código | Nombre |")
    print("| :--- | :--- | :--- |")
    # Sort numeric ones, ignore or print non-numeric at end
    valid_aids = []
    for aid in airline_ids:
        if aid.isdigit():
            valid_aids.append(int(aid))
    
    for aid in sorted(valid_aids):
        name = airlines.get(str(aid), "Unknown")
        parts = name.split(' - ')
        if len(parts) > 1:
             print(f"| {aid} | {parts[0]} | {parts[1]} |")
        else:
             print(f"| {aid} | {name} | |")

    print("\n### Orígenes Soportados (Available as 'origin')")
    print("*(Muestra primeros 25)*")
    print("| ID | IATA | Detalle |")
    print("| :--- | :--- | :--- |")
    
    valid_oids = []
    for oid in origin_ids:
        if oid.isdigit():
            valid_oids.append(int(oid))
            
    count = 0
    for oid in sorted(valid_oids):
        detail = airports.get(str(oid), f"Unknown ID {oid}")
        parts = detail.split(' - ')
        iata = parts[0] if len(parts) > 1 else "?"
        name = parts[1] if len(parts) > 1 else detail
        print(f"| {oid} | {iata} | {name} |")
        
        count += 1
        if count >= 25: 
             break
    
    print(f"\n... (Total {len(valid_oids)} orígenes numéricos válidos)")

    print("\n### Destinos Soportados (Available as 'destination')")
    print("*(Muestra primeros 25)*")
    print("| ID | IATA | Detalle |")
    print("| :--- | :--- | :--- |")
    
    valid_dids = []
    for did in dest_ids:
        if did.isdigit():
            valid_dids.append(int(did))
            
    count = 0
    for did in sorted(valid_dids):
        detail = airports.get(str(did), f"Unknown ID {did}")
        parts = detail.split(' - ')
        iata = parts[0] if len(parts) > 1 else "?"
        name = parts[1] if len(parts) > 1 else detail
        print(f"| {did} | {iata} | {name} |")
        count += 1
        if count >= 25:
            break
    print(f"\n... (Total {len(valid_dids)} destinos numéricos válidos)")

if __name__ == "__main__":
    main()

