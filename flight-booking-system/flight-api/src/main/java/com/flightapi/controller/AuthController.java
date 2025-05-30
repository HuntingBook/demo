package com.flightapi.controller;

import com.flightapi.dto.AuthResponseDTO;
import com.flightapi.dto.BaseResponse;
import com.flightapi.dto.LoginRequestDTO;
import com.flightapi.dto.RegisterRequestDTO;
import com.flightapi.exception.EmailAlreadyExistsException;
import com.flightapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication-related requests, such as user registration and login.
 * All endpoints in this controller are under the "/api/auth" path.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Service responsible for authentication logic, including user registration and login.
     */
    @Autowired
    private AuthService authService;

    /**
     * Handles user registration requests.
     * Accepts registration details, validates them, and attempts to register a new user.
     *
     * @param registerRequestDTO DTO containing user registration information (e.g., username, email, password).
     * @return A {@link ResponseEntity} containing a {@link BaseResponse}.
     *         On success (HTTP 201 CREATED): {@link AuthResponseDTO} with JWT and user details.
     *         On email conflict (HTTP 409 CONFLICT): Error message indicating the email already exists.
     *         On validation failure: Spring's default validation error response (typically HTTP 400 BAD REQUEST).
     */
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<AuthResponseDTO>> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            AuthResponseDTO authResponse = authService.registerUser(registerRequestDTO);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(BaseResponse.success(authResponse, "User registered successfully"));
        } catch (EmailAlreadyExistsException e) {
            // This is a specific business logic exception, could be handled by a global handler too
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 Conflict
                    .body(BaseResponse.error(HttpStatus.CONFLICT.value(), e.getMessage()));
        }
        // Other potential exceptions like validation failures will be handled by Spring's default mechanisms
        // or a global exception handler if implemented.
    }

    /**
     * Handles user login requests.
     * Accepts login credentials, validates them, and attempts to authenticate the user.
     *
     * @param loginRequestDTO DTO containing user login credentials (e.g., email, password).
     * @return A {@link ResponseEntity} containing a {@link BaseResponse}.
     *         On success (HTTP 200 OK): {@link AuthResponseDTO} with JWT and user details.
     *         On authentication failure (e.g., bad credentials): Spring Security handles this, typically returning
     *         an HTTP 401 UNAUTHORIZED or 403 FORBIDDEN response, depending on global exception handling.
     *         On validation failure: Spring's default validation error response (typically HTTP 400 BAD REQUEST).
     */
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponseDTO>> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        // Spring Security's AuthenticationManager will handle BadCredentialsException if login fails,
        // which typically results in a 401 or 403 error depending on global exception handling.
        AuthResponseDTO authResponse = authService.loginUser(loginRequestDTO);
        return ResponseEntity.ok(BaseResponse.success(authResponse, "User logged in successfully"));
    }
}
