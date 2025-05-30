package com.flightapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for user registration requests.
 * Contains the necessary information for a new user to register, such as name, email, and password.
 * Includes validation annotations to ensure data integrity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    /**
     * The email address of the user to be registered.
     * This field is mandatory and must be a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * The password for the new user account.
     * This field is mandatory and must be at least 6 characters long.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    /**
     * The first name of the user to be registered.
     * This field is mandatory.
     */
    @NotBlank(message = "First name is required")
    private String firstName;

    /**
     * The last name of the user to be registered.
     * This field is mandatory.
     */
    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Country is required")
    private String country;

    private String phone; // Optional
}
