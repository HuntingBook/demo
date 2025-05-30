package com.flightapi.controller;

import com.flightapi.dto.BaseResponse;
import com.flightapi.dto.FlightDTO;
import com.flightapi.dto.FlightSearchRequestDTO; // Using this for validation via @Valid
import com.flightapi.exception.AirportNotFoundException;
import com.flightapi.exception.InvalidDateFormatException;
import com.flightapi.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for handling flight-related requests, primarily for searching flights.
 * All endpoints in this controller are under the "/api/flights" path.
 * The class is annotated with {@link Validated} to enable validation of request parameters.
 */
@RestController
@RequestMapping("/api/flights")
@Validated // Required for validating request parameters if not using a DTO
public class FlightController {

    /**
     * Service responsible for flight search logic and data retrieval.
     */
    @Autowired
    private FlightService flightService;

    /**
     * Searches for flights based on departure airport, arrival airport, and departure date.
     * Uses {@link FlightSearchRequestDTO} to encapsulate and validate search parameters.
     *
     * @param searchRequest A DTO containing the search criteria: fromAirportCode, toAirportCode, and departureDate.
     *                      This DTO is validated using {@link Valid}.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with a list of {@link FlightDTO}s.
     *         On success (HTTP 200 OK): A list of matching flights or an empty list if no flights are found.
     *         On airport not found (HTTP 404 NOT_FOUND): Error message indicating the airport code was invalid.
     *         On invalid date format (HTTP 400 BAD_REQUEST): Error message indicating the date format was incorrect.
     *         On other validation errors (HTTP 400 BAD_REQUEST): Error message detailing the validation failure.
     */
    @GetMapping
    public ResponseEntity<BaseResponse<List<FlightDTO>>> searchFlights(
            @Valid FlightSearchRequestDTO searchRequest // Using a DTO for request params for better validation
    ) {
        try {
            List<FlightDTO> flights = flightService.searchFlights(
                    searchRequest.getFromAirportCode(),
                    searchRequest.getToAirportCode(),
                    searchRequest.getDepartureDate()
            );
            if (flights.isEmpty()) {
                return ResponseEntity.ok(BaseResponse.success(flights, "No flights found matching your criteria."));
            }
            return ResponseEntity.ok(BaseResponse.success(flights, "Flights retrieved successfully."));
        } catch (AirportNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (InvalidDateFormatException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (IllegalArgumentException e) { // Catching validation errors from service
             return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
        // General exceptions will be caught by a global handler (to be implemented)
    }
}
