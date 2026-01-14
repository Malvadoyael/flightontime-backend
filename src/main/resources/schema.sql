CREATE TABLE IF NOT EXISTS flight_match (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aerolinea_id INT,
    origen_id INT,
    destino_id INT
);