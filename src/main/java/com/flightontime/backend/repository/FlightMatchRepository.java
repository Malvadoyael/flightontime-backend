package com.flightontime.backend.repository;

import com.flightontime.backend.model.AirlineOriginProjection;
import com.flightontime.backend.model.FlightMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlightMatchRepository extends JpaRepository<FlightMatch, Long> {

    @Query(value = "SELECT DISTINCT " +
            "     AL.ID AS id_aero, " +
            "    AL.FULL_NAME AS name_aero, " +
            "    FM.ORIGEN_ID id_origen, " +
            "    A_ORIGEN.NAME AS Aer_origen " +
            "FROM " +
            "    FLIGHT_MATCH FM " +
            "JOIN " +
            "    AIRLINE AL ON FM.AEROLINEA_ID = AL.ID " +
            "JOIN " +
            "    AIRPORT A_ORIGEN ON FM.ORIGEN_ID = A_ORIGEN.ID " +
            "WHERE " +
            "    FM.AEROLINEA_ID = :airlineId " +
            "ORDER BY " +
            "Aer_origen", nativeQuery = true)
    List<AirlineOriginProjection> findDestinationsByAirlineId(@Param("airlineId") Integer airlineId);
}
