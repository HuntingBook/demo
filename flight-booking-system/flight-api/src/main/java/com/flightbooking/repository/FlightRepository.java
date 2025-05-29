package com.flightbooking.repository;

import com.flightbooking.entity.Airport;
import com.flightbooking.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    // Using Airport entities as parameters
    List<Flight> findByDepartureAirportAndDestinationAirportAndDepartureDate(
            Airport departureAirport, Airport destinationAirport, LocalDate departureDate);

    // Alternative: Using airport IDs as parameters
    List<Flight> findByDepartureAirport_AirportIdAndDestinationAirport_AirportIdAndDepartureDate(
            Long departureAirportId, Long destinationAirportId, LocalDate departureDate);
}
