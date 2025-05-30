package com.flightapi.controller;

import com.flightapi.dto.BaseResponse;
import com.flightapi.dto.BookingDTO;
import com.flightapi.dto.BookingRequestDTO;
import com.flightapi.dto.PaginatedResponseDTO;
import com.flightapi.exception.BookingNotFoundException;
import com.flightapi.exception.EmptyPassengerListException;
import com.flightapi.exception.FlightNotFoundException;
import com.flightapi.exception.UserNotFoundException;
import com.flightapi.service.BookingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling flight booking operations.
 * Allows authenticated users to create new bookings, retrieve their existing bookings (with optional status filtering and pagination),
 * and fetch specific booking details by reference number.
 * All endpoints in this controller are under the "/api/bookings" path and require authentication.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    /**
     * Service responsible for booking-related business logic, including creation and retrieval of bookings.
     */
    @Autowired
    private BookingService bookingService;

    /**
     * Creates a new flight booking for the authenticated user.
     * The user's email is obtained from the security context.
     *
     * @param bookingRequestDTO DTO containing the details for the new booking, including flight ID and passenger information.
     *                          This DTO is validated using {@link Valid}.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with the created {@link BookingDTO}.
     *         On success (HTTP 201 CREATED): The created booking details.
     *         On user or flight not found (HTTP 404 NOT_FOUND): Error message indicating the issue.
     *         On empty passenger list (HTTP 400 BAD_REQUEST): Error message indicating no passengers were provided.
     *         On validation failure: Spring's default validation error response (typically HTTP 400 BAD_REQUEST).
     */
    @PostMapping
    public ResponseEntity<BaseResponse<BookingDTO>> createBooking(@Valid @RequestBody BookingRequestDTO bookingRequestDTO) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Received booking creation request for user: {}", userEmail);
        try {
            BookingDTO bookingDTO = bookingService.createBooking(bookingRequestDTO, userEmail);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(BaseResponse.success(bookingDTO, "Booking created successfully."));
        } catch (UserNotFoundException | FlightNotFoundException e) {
            logger.warn("Error creating booking: User or Flight not found. {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (EmptyPassengerListException e) {
            logger.warn("Error creating booking: Empty passenger list. {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
        // Other exceptions (e.g., validation) will be handled by global handler or Spring's default.
    }

    /**
     * Retrieves a paginated list of bookings for the authenticated user.
     * Bookings can be optionally filtered by status (e.g., "CONFIRMED", "CANCELLED").
     * The user's email is obtained from the security context.
     *
     * @param status Optional. The status of bookings to filter by.
     * @param page The page number for pagination (0-indexed, default is 0).
     * @param size The number of bookings per page (default is 10).
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with a {@link PaginatedResponseDTO} of {@link BookingDTO}s.
     *         On success (HTTP 200 OK): A paginated list of the user's bookings.
     *         On user not found (HTTP 404 NOT_FOUND): Error message (should ideally not happen if user is authenticated).
     */
    @GetMapping
    public ResponseEntity<BaseResponse<PaginatedResponseDTO<BookingDTO>>> getUserBookings(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Received request to get bookings for user: {}, status: {}, page: {}, size: {}", userEmail, status, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("bookingTime").descending());

        try {
            PaginatedResponseDTO<BookingDTO> bookings = bookingService.getUserBookings(userEmail, status, pageable);
            return ResponseEntity.ok(BaseResponse.success(bookings, "Bookings retrieved successfully."));
        } catch (UserNotFoundException e) {
            logger.warn("Error fetching bookings: User not found. {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }
    }

    /**
     * Retrieves a specific booking by its reference number for the authenticated user.
     * Ensures that the retrieved booking belongs to the currently authenticated user.
     * The user's email is obtained from the security context.
     *
     * @param reference The unique reference number of the booking to retrieve.
     * @return A {@link ResponseEntity} containing a {@link BaseResponse} with the {@link BookingDTO}.
     *         On success (HTTP 200 OK): The details of the requested booking.
     *         On user or booking not found (HTTP 404 NOT_FOUND): Error message indicating the booking or user was not found,
     *         or the booking does not belong to the user.
     */
    @GetMapping("/{reference}")
    public ResponseEntity<BaseResponse<BookingDTO>> getBookingByReference(@PathVariable String reference) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Received request to get booking by reference: {} for user: {}", reference, userEmail);
        try {
            BookingDTO bookingDTO = bookingService.getBookingByReference(reference, userEmail);
            return ResponseEntity.ok(BaseResponse.success(bookingDTO, "Booking details retrieved successfully."));
        } catch (UserNotFoundException | BookingNotFoundException e) {
            logger.warn("Error fetching booking by reference: {}. {}", reference, e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }
    }
}
