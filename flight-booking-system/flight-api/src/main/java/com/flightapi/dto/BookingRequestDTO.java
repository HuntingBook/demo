package com.flightapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) for creating a new flight booking.
 * Contains the necessary information to initiate a booking, such as the flight ID and passenger details.
 * Includes validation annotations to ensure data integrity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    /**
     * The unique identifier of the flight to be booked.
     * This field is mandatory.
     */
    @NotNull(message = "Flight ID is required")
    private Long flightId;

    /**
     * A list of passengers for whom the booking is being made.
     * At least one passenger must be provided. Each {@link PassengerDTO} in the list will also be validated.
     * See {@link PassengerDTO}.
     */
    @NotEmpty(message = "At least one passenger is required")
    @Valid // Ensures validation of PassengerDTO objects within the list
    private List<PassengerDTO> passengers;
}
