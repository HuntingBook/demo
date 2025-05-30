package com.flightapi.repository;

import com.flightapi.entity.Airport;
import com.flightapi.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for {@link Flight} entities.
 * Provides CRUD operations and custom query methods for accessing flight data.
 */
@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    /**
     * Finds flights based on departure airport, destination airport, and departure date.
     *
     * @param departureAirport   The {@link Airport} entity representing the departure airport.
     * @param destinationAirport The {@link Airport} entity representing the destination airport.
     * @param departureDate      The {@link LocalDate} representing the departure date.
     * @return A list of {@link Flight} entities matching the criteria.
     */
    List<Flight> findByDepartureAirportAndDestinationAirportAndDepartureDate(
            Airport departureAirport, Airport destinationAirport, LocalDate departureDate);

    /**
     * Finds flights based on departure airport ID, destination airport ID, and departure date.
     * This is an alternative to {@link #findByDepartureAirportAndDestinationAirportAndDepartureDate(Airport, Airport, LocalDate)}
     * when only airport IDs are available.
     *
     * @param departureAirportId   The ID of the departure airport.
     * @param destinationAirportId The ID of the destination airport.
     * @param departureDate        The {@link LocalDate} representing the departure date.
     * @return A list of {@link Flight} entities matching the criteria.
     */
    List<Flight> findByDepartureAirport_AirportIdAndDestinationAirport_AirportIdAndDepartureDate(
            Long departureAirportId, Long destinationAirportId, LocalDate departureDate);
}
