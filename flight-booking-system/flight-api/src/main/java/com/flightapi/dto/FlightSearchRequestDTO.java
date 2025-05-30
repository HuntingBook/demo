package com.flightapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for flight search requests.
 * Contains the criteria for searching flights, such as departure and destination airport codes, and departure date.
 * Includes validation annotations to ensure data integrity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchRequestDTO {

    /**
     * The IATA code of the departure airport (e.g., "JFK").
     * This field is mandatory.
     */
    @NotBlank(message = "Departure airport code is required")
    private String fromAirportCode;

    /**
     * The IATA code of the destination airport (e.g., "LAX").
     * This field is mandatory.
     */
    @NotBlank(message = "Destination airport code is required")
    private String toAirportCode;

    /**
     * The desired departure date in "yyyy-MM-dd" format.
     * This field is mandatory and must match the specified pattern.
     */
    @NotBlank(message = "Departure date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Departure date must be in yyyy-MM-dd format")
    private String departureDate;
}
