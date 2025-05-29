package com.flightbooking.service;

import com.flightbooking.dto.*;
import com.flightbooking.entity.Booking;
import com.flightbooking.entity.Flight;
import com.flightbooking.entity.Passenger;
import com.flightbooking.entity.User;
import com.flightbooking.exception.BookingNotFoundException;
import com.flightbooking.exception.EmptyPassengerListException;
import com.flightbooking.exception.FlightNotFoundException;
import com.flightbooking.exception.UserNotFoundException;
import com.flightbooking.repository.BookingRepository;
import com.flightbooking.repository.FlightRepository;
import com.flightbooking.repository.PassengerRepository;
import com.flightbooking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassengerRepository passengerRepository; // PassengerRepository might not be explicitly used if relying on cascade.

    @Transactional
    public BookingDTO createBooking(BookingRequestDTO bookingRequestDTO, String authenticatedUserEmail) {
        logger.info("Creating booking for user: {} and flight ID: {}", authenticatedUserEmail, bookingRequestDTO.getFlightId());

        User user = userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> {
                    logger.warn("User not found with email: {}", authenticatedUserEmail);
                    return new UserNotFoundException("User not found with email: " + authenticatedUserEmail);
                });

        Flight flight = flightRepository.findById(bookingRequestDTO.getFlightId())
                .orElseThrow(() -> {
                    logger.warn("Flight not found with ID: {}", bookingRequestDTO.getFlightId());
                    return new FlightNotFoundException("Flight not found with ID: " + bookingRequestDTO.getFlightId());
                });

        if (CollectionUtils.isEmpty(bookingRequestDTO.getPassengers())) {
            logger.warn("Passenger list is empty for booking request by user: {}", authenticatedUserEmail);
            throw new EmptyPassengerListException("Passenger list cannot be empty.");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);

        // Generate unique booking reference
        String reference;
        do {
            reference = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        } while (bookingRepository.findByReference(reference).isPresent());
        booking.setReference(reference);
        logger.info("Generated unique booking reference: {}", reference);

        booking.setStatus("CONFIRMED"); // Initial status

        // For simplicity, flight.getPrice() is the total.
        // If flight.getPrice() is per passenger:
        // BigDecimal totalPrice = flight.getPrice().multiply(BigDecimal.valueOf(bookingRequestDTO.getPassengers().size()));
        booking.setTotalPrice(flight.getPrice());

        List<Passenger> passengerEntities = new ArrayList<>();
        for (PassengerDTO passengerDTO : bookingRequestDTO.getPassengers()) {
            Passenger passenger = new Passenger();
            passenger.setFirstName(passengerDTO.getFirstName());
            passenger.setLastName(passengerDTO.getLastName());
            passenger.setEmail(passengerDTO.getEmail());
            passenger.setBooking(booking); // Link passenger to this booking
            passengerEntities.add(passenger);
        }
        booking.setPassengers(passengerEntities);

        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking saved successfully with ID: {} and reference: {}", savedBooking.getBookingId(), savedBooking.getReference());

        return BookingDTO.fromBooking(savedBooking);
    }

    @Transactional(readOnly = true)
    public PaginatedResponseDTO<BookingDTO> getUserBookings(String authenticatedUserEmail, String status, Pageable pageable) {
        logger.info("Fetching bookings for user: {} with status: {} and pageable: {}", authenticatedUserEmail, status, pageable);

        User user = userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> {
                    logger.warn("User not found with email: {}", authenticatedUserEmail);
                    return new UserNotFoundException("User not found with email: " + authenticatedUserEmail);
                });

        Page<Booking> bookingPage;
        if (StringUtils.hasText(status) && !"ALL".equalsIgnoreCase(status)) {
            // Assuming status directly maps to Booking.status (e.g., "CONFIRMED", "CANCELLED")
            // For "UPCOMING" / "PAST" logic based on flight date, more complex queries/logic would be needed.
            // This is the simplified approach.
            bookingPage = bookingRepository.findByUserAndStatusOrderByBookingTimeDesc(user, status.toUpperCase(), pageable);
            logger.debug("Fetched bookings by user and status '{}'", status.toUpperCase());
        } else {
            bookingPage = bookingRepository.findByUserOrderByBookingTimeDesc(user, pageable);
            logger.debug("Fetched all bookings by user");
        }

        List<BookingDTO> bookingDTOs = bookingPage.getContent().stream()
                .map(BookingDTO::fromBooking)
                .collect(Collectors.toList());

        logger.info("Retrieved {} bookings for user {} on page {}", bookingDTOs.size(), authenticatedUserEmail, pageable.getPageNumber());
        return new PaginatedResponseDTO<>(bookingPage.map(BookingDTO::fromBooking)); // Use Page.map for concise conversion
    }

    @Transactional(readOnly = true)
    public BookingDTO getBookingByReference(String reference, String authenticatedUserEmail) {
        logger.info("Fetching booking by reference: {} for user: {}", reference, authenticatedUserEmail);
        User user = userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + authenticatedUserEmail));

        Booking booking = bookingRepository.findByReference(reference)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with reference: " + reference));

        // Optional: Check if the booking belongs to the authenticated user
        if (!booking.getUser().getUserId().equals(user.getUserId())) {
            logger.warn("User {} attempted to access booking {} which does not belong to them.", authenticatedUserEmail, reference);
            // Depending on security policy, either throw BookingNotFound or a specific access denied exception
            throw new BookingNotFoundException("Booking not found with reference: " + reference);
        }

        return BookingDTO.fromBooking(booking);
    }
}
