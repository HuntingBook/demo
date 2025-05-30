package com.flightapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for authentication responses.
 * Contains the JWT token and user details upon successful authentication (login or registration).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    /**
     * The JSON Web Token (JWT) generated for the authenticated user.
     */
    private String token;
    /**
     * DTO containing details of the authenticated user (e.g., ID, email, name).
     * See {@link UserDTO}.
     */
    private UserDTO user;
}
