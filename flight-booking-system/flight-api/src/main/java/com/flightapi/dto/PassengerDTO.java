package com.flightapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for representing passenger information when creating a booking.
 * Contains the passenger's first name, last name, and email.
 * Includes validation annotations to ensure data integrity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {

    /**
     * The first name of the passenger.
     * This field is mandatory.
     */
    @NotBlank(message = "First name is required")
    private String firstName;

    /**
     * The last name of the passenger.
     * This field is mandatory.
     */
    @NotBlank(message = "Last name is required")
    private String lastName;

    /**
     * The email address of the passenger.
     * This field is mandatory and must be a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
}
