package com.flightbooking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotEmpty(message = "At least one passenger is required")
    @Valid // Ensures validation of PassengerDTO objects within the list
    private List<PassengerDTO> passengers;
}
