package com.flightapi.service;

import com.flightapi.dto.*;
import com.flightapi.entity.Booking;
import com.flightapi.entity.Flight;
import com.flightapi.entity.Passenger;
import com.flightapi.entity.User;
import com.flightapi.exception.BookingNotFoundException;
import com.flightapi.exception.EmptyPassengerListException;
import com.flightapi.exception.FlightNotFoundException;
import com.flightapi.exception.UserNotFoundException;
import com.flightapi.repository.BookingRepository;
import com.flightapi.repository.FlightRepository;
import com.flightapi.repository.PassengerRepository;
import com.flightapi.repository.UserRepository;
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

/**
 * Service class for managing flight bookings.
 * Handles operations such as creating new bookings, retrieving user bookings, and fetching booking details.
 */
@Service
public class BookingService {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    /**
     * Repository for booking data access.
     */
    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Repository for flight data access.
     */
    @Autowired
    private FlightRepository flightRepository;

    /**
     * Repository for user data access.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Repository for passenger data access. May not be explicitly used if relying on cascade persistence for passengers.
     */
    @Autowired
    private PassengerRepository passengerRepository; // PassengerRepository might not be explicitly used if relying on cascade.

    /**
     * Creates a new booking for a flight.
     *
     * @param bookingRequestDTO DTO containing the details for the new booking.
     * @param authenticatedUserEmail The email of the authenticated user making the booking.
     * @return BookingDTO representing the newly created booking.
     * @throws UserNotFoundException if the authenticated user is not found.
     * @throws FlightNotFoundException if the specified flight is not found.
     * @throws EmptyPassengerListException if the passenger list in the request is empty.
     */
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

    /**
     * Retrieves a paginated list of bookings for a specific user, optionally filtered by status.
     *
     * @param authenticatedUserEmail The email of the authenticated user whose bookings are to be retrieved.
     * @param status Optional status to filter bookings by (e.g., "CONFIRMED", "CANCELLED", "ALL").
     * @param pageable Pagination information.
     * @return PaginatedResponseDTO containing a list of BookingDTOs.
     * @throws UserNotFoundException if the authenticated user is not found.
     */
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

    /**
     * Retrieves a specific booking by its reference number for the authenticated user.
     *
     * @param reference The unique reference number of the booking.
     * @param authenticatedUserEmail The email of the authenticated user requesting the booking details.
     * @return BookingDTO representing the found booking.
     * @throws UserNotFoundException if the authenticated user is not found.
     * @throws BookingNotFoundException if the booking with the given reference is not found or does not belong to the user.
     */
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
