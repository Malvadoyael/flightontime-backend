package com.flightontime.backend.repository;

import com.flightontime.backend.model.Airline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {

    @Query(value = "SELECT * FROM airline  WHERE active = :active", nativeQuery = true)
    List<Airline> getAirlineByActive(@Param("active") String active);
}