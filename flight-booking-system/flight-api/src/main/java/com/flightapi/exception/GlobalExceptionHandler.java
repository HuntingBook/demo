package com.flightapi.exception;

import com.flightapi.dto.BaseResponse;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        logger.warn("Validation failed: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error(HttpStatus.BAD_REQUEST.value(), "Validation failed"));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<BaseResponse<String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        logger.warn("EmailAlreadyExistsException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class) // Handles Spring Security's bad credentials
    public ResponseEntity<BaseResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {
        logger.warn("BadCredentialsException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"));
    }

    @ExceptionHandler(InvalidCredentialsException.class) // Custom invalid credentials, if used
    public ResponseEntity<BaseResponse<String>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        logger.warn("InvalidCredentialsException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }

    @ExceptionHandler(AirportNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleAirportNotFoundException(AirportNotFoundException ex) {
        logger.warn("AirportNotFoundException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleFlightNotFoundException(FlightNotFoundException ex) {
        logger.warn("FlightNotFoundException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleUserNotFoundException(UserNotFoundException ex) {
        logger.warn("UserNotFoundException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleBookingNotFoundException(BookingNotFoundException ex) {
        logger.warn("BookingNotFoundException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidDateFormatException(InvalidDateFormatException ex) {
        logger.warn("InvalidDateFormatException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(EmptyPassengerListException.class)
    public ResponseEntity<BaseResponse<String>> handleEmptyPassengerListException(EmptyPassengerListException ex) {
        logger.warn("EmptyPassengerListException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(JwtException.class) // General JWT issues from io.jsonwebtoken
    public ResponseEntity<BaseResponse<String>> handleJwtException(JwtException ex) {
        logger.warn("JwtException: {}", ex.getMessage());
        // Specific exceptions like ExpiredJwtException, SignatureException etc. are subclasses of JwtException
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired JWT token: " + ex.getMessage()));
    }
}
