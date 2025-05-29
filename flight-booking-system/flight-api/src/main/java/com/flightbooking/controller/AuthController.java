package com.flightbooking.controller;

import com.flightbooking.dto.AuthResponseDTO;
import com.flightbooking.dto.BaseResponse;
import com.flightbooking.dto.LoginRequestDTO;
import com.flightbooking.dto.RegisterRequestDTO;
import com.flightbooking.exception.EmailAlreadyExistsException;
import com.flightbooking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

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

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponseDTO>> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        // Spring Security's AuthenticationManager will handle BadCredentialsException if login fails,
        // which typically results in a 401 or 403 error depending on global exception handling.
        AuthResponseDTO authResponse = authService.loginUser(loginRequestDTO);
        return ResponseEntity.ok(BaseResponse.success(authResponse, "User logged in successfully"));
    }
}
