package com.flightapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for user login requests.
 * Contains the credentials (email and password) required for a user to log in.
 * Includes validation annotations to ensure data integrity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    /**
     * The email address of the user attempting to log in.
     * This field is mandatory and must be a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * The password of the user attempting to log in.
     * This field is mandatory.
     */
    @NotBlank(message = "Password is required")
    private String password;
}
