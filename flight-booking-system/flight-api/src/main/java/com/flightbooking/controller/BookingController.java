package com.flightbooking.controller;

import com.flightbooking.dto.BaseResponse;
import com.flightbooking.dto.BookingDTO;
import com.flightbooking.dto.BookingRequestDTO;
import com.flightbooking.dto.PaginatedResponseDTO;
import com.flightbooking.exception.BookingNotFoundException;
import com.flightbooking.exception.EmptyPassengerListException;
import com.flightbooking.exception.FlightNotFoundException;
import com.flightbooking.exception.UserNotFoundException;
import com.flightbooking.service.BookingService;
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

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

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
