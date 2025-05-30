package com.flightapi.service;

import com.flightapi.dto.FlightDTO;
import com.flightapi.entity.Airport;
import com.flightapi.entity.Flight;
import com.flightapi.exception.AirportNotFoundException;
import com.flightapi.exception.InvalidDateFormatException;
import com.flightapi.repository.AirportRepository;
import com.flightapi.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling flight-related operations, such as searching for flights.
 */
@Service
public class FlightService {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);
    /**
     * Formatter for parsing date strings in ISO_LOCAL_DATE format (yyyy-MM-dd).
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    /**
     * Repository for flight data access.
     */
    @Autowired
    private FlightRepository flightRepository;

    /**
     * Repository for airport data access.
     */
    @Autowired
    private AirportRepository airportRepository;

    /**
     * Searches for flights based on departure airport, destination airport, and departure date.
     *
     * @param fromAirportCode The code of the departure airport.
     * @param toAirportCode The code of the destination airport.
     * @param departureDateStr The departure date in yyyy-MM-dd format.
     * @return A list of FlightDTOs matching the search criteria.
     * @throws IllegalArgumentException if any of the input parameters are blank.
     * @throws InvalidDateFormatException if the departure date string is not in the expected format.
     * @throws AirportNotFoundException if the departure or destination airport code is not found.
     */
    @Transactional(readOnly = true)
    public List<FlightDTO> searchFlights(String fromAirportCode, String toAirportCode, String departureDateStr) {
        // Validate input parameters
        if (!StringUtils.hasText(fromAirportCode)) {
            throw new IllegalArgumentException("Departure airport code must not be blank.");
        }
        if (!StringUtils.hasText(toAirportCode)) {
            throw new IllegalArgumentException("Destination airport code must not be blank.");
        }
        if (!StringUtils.hasText(departureDateStr)) {
            throw new IllegalArgumentException("Departure date must not be blank.");
        }

        LocalDate parsedDepartureDate;
        try {
            parsedDepartureDate = LocalDate.parse(departureDateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format for departure date: {}", departureDateStr, e);
            throw new InvalidDateFormatException("Invalid date format. Please use yyyy-MM-dd.", e);
        }

        // Fetch departure and destination airports
        Airport departureAirport = airportRepository.findByCode(fromAirportCode)
                .orElseThrow(() -> {
                    logger.warn("Departure airport not found with code: {}", fromAirportCode);
                    return new AirportNotFoundException("Departure airport not found: " + fromAirportCode);
                });

        Airport destinationAirport = airportRepository.findByCode(toAirportCode)
                .orElseThrow(() -> {
                    logger.warn("Destination airport not found with code: {}", toAirportCode);
                    return new AirportNotFoundException("Destination airport not found: " + toAirportCode);
                });

        // Call flight repository
        List<Flight> flights = flightRepository.findByDepartureAirportAndDestinationAirportAndDepartureDate(
                departureAirport, destinationAirport, parsedDepartureDate
        );

        if (flights.isEmpty()) {
            logger.info("No flights found from {} to {} on {}", fromAirportCode, toAirportCode, parsedDepartureDate);
            return Collections.emptyList();
        }

        // Convert Flight entities to FlightDTOs
        return flights.stream()
                .map(FlightDTO::fromFlight)
                .collect(Collectors.toList());
    }
}
