package com.flightbooking.service;

import com.flightbooking.dto.FlightDTO;
import com.flightbooking.entity.Airport;
import com.flightbooking.entity.Flight;
import com.flightbooking.exception.AirportNotFoundException;
import com.flightbooking.exception.InvalidDateFormatException;
import com.flightbooking.repository.AirportRepository;
import com.flightbooking.repository.FlightRepository;
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

@Service
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

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
