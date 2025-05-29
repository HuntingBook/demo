package com.flightbooking.controller;

import com.flightbooking.dto.BaseResponse;
import com.flightbooking.dto.FlightDTO;
import com.flightbooking.dto.FlightSearchRequestDTO; // Using this for validation via @Valid
import com.flightbooking.exception.AirportNotFoundException;
import com.flightbooking.exception.InvalidDateFormatException;
import com.flightbooking.service.FlightService;
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

@RestController
@RequestMapping("/api/flights")
@Validated // Required for validating request parameters if not using a DTO
public class FlightController {

    @Autowired
    private FlightService flightService;

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
