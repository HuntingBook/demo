package com.flightbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchRequestDTO {

    @NotBlank(message = "Departure airport code is required")
    private String fromAirportCode;

    @NotBlank(message = "Destination airport code is required")
    private String toAirportCode;

    @NotBlank(message = "Departure date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Departure date must be in yyyy-MM-dd format")
    private String departureDate;
}
