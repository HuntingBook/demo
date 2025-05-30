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

/**
 * Global controller advice to handle exceptions across the whole application.
 * This class catches defined exceptions and returns appropriate HTTP responses.
 */
@ControllerAdvice
public class Ocs {

    /**
     * Logger instance for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(Ocs.class);

    /**
     * Handles generic {@link Exception} instances that are not specifically caught by other handlers.
     * Logs the error and returns an HTTP 500 Internal Server Error response.
     *
     * @param ex The exception that was thrown.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
    }

    /**
     * Handles {@link MethodArgumentNotValidException} which occurs when validation on an argument annotated with @Valid fails.
     * Returns an HTTP 400 Bad Request response with a map of field errors.
     *
     * @param ex The exception containing validation errors.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with validation error details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        logger.warn("Validation failed: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors));
    }

    /**
     * Handles {@link EmailAlreadyExistsException} when a registration attempt is made with an email that already exists.
     * Returns an HTTP 409 Conflict response.
     *
     * @param ex The {@link EmailAlreadyExistsException} instance.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with the error message.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<BaseResponse<String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        logger.warn("EmailAlreadyExistsException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.error(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    /**
     * Handles {@link BadCredentialsException} from Spring Security, typically thrown for authentication failures.
     * Returns an HTTP 401 Unauthorized response.
     *
     * @param ex The {@link BadCredentialsException} instance.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with an invalid credentials message.
     */
    @ExceptionHandler(BadCredentialsException.class) // Handles Spring Security's bad credentials
    public ResponseEntity<BaseResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {
        logger.warn("BadCredentialsException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"));
    }

    /**
     * Handles custom {@link InvalidCredentialsException}, if used for more specific credential errors.
     * Returns an HTTP 401 Unauthorized response.
     *
     * @param ex The {@link InvalidCredentialsException} instance.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with the error message.
     */
    @ExceptionHandler(InvalidCredentialsException.class) // Custom invalid credentials, if used
    public ResponseEntity<BaseResponse<String>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        logger.warn("InvalidCredentialsException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
    }

    /**
     * Handles {@link AirportNotFoundException} when an airport lookup fails.
     * Returns an HTTP 404 Not Found response.
     *
     * @param ex The {@link AirportNotFoundException} instance.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with the error message.
     */
    @ExceptionHandler(AirportNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleAirportNotFoundException(AirportNotFoundException ex) {
        logger.warn("AirportNotFoundException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    /**
     * Handles {@link FlightNotFoundException} when a flight lookup fails.
     * Returns an HTTP 404 Not Found response.
     *
     * @param ex The {@link FlightNotFoundException} instance.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with the error message.
     */
    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleFlightNotFoundException(FlightNotFoundException ex) {
        logger.warn("FlightNotFoundException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    /**
     * Handles {@link UserNotFoundException} when a user lookup fails.
     * Returns an HTTP 404 Not Found response.
     *
     * @param ex The {@link UserNotFoundException} instance.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with the error message.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleUserNotFoundException(UserNotFoundException ex) {
        logger.warn("UserNotFoundException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    /**
     * Handles {@link BookingNotFoundException} when a booking lookup fails.
     * Returns an HTTP 404 Not Found response.
     *
     * @param ex The {@link BookingNotFoundException} instance.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with the error message.
     */
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleBookingNotFoundException(BookingNotFoundException ex) {
        logger.warn("BookingNotFoundException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    /**
     * Handles {@link InvalidDateFormatException} when a date string cannot be parsed correctly.
     * Returns an HTTP 400 Bad Request response.
     *
     * @param ex The {@link InvalidDateFormatException} instance.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with the error message.
     */
    @ExceptionHandler(InvalidDateFormatException.class)
    public ResponseEntity<BaseResponse<String>> handleInvalidDateFormatException(InvalidDateFormatException ex) {
        logger.warn("InvalidDateFormatException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    /**
     * Handles {@link EmptyPassengerListException} when a booking is attempted with no passengers.
     * Returns an HTTP 400 Bad Request response.
     *
     * @param ex The {@link EmptyPassengerListException} instance.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with the error message.
     */
    @ExceptionHandler(EmptyPassengerListException.class)
    public ResponseEntity<BaseResponse<String>> handleEmptyPassengerListException(EmptyPassengerListException ex) {
        logger.warn("EmptyPassengerListException: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    /**
     * Handles generic {@link JwtException} instances from the `io.jsonwebtoken` library, covering various JWT processing errors.
     * Returns an HTTP 401 Unauthorized response.
     *
     * @param ex The {@link JwtException} instance (e.g., {@link io.jsonwebtoken.ExpiredJwtException}, {@link io.jsonwebtoken.SignatureException}).
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with an error message about the invalid/expired token.
     */
    @ExceptionHandler(JwtException.class) // General JWT issues from io.jsonwebtoken
    public ResponseEntity<BaseResponse<String>> handleJwtException(JwtException ex) {
        logger.warn("JwtException: {}", ex.getMessage());
        // Specific exceptions like ExpiredJwtException, SignatureException etc. are subclasses of JwtException
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.error(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired JWT token: " + ex.getMessage()));
    }
}
