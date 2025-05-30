package com.flightapi.dto;

import com.flightapi.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object (DTO) for representing flight booking details.
 * This DTO is used to transfer booking information between layers and in API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    /**
     * The unique identifier for the booking.
     */
    private Long bookingId;
    /**
     * A unique reference number for the booking (e.g., PNR).
     */
    private String reference;
    /**
     * DTO containing details of the booked flight.
     * See {@link FlightDTO}.
     */
    private FlightDTO flightInfo;
    /**
     * DTO containing details of the user who made the booking.
     * See {@link UserDTO}.
     */
    private UserDTO userInfo;
    /**
     * The current status of the booking (e.g., "CONFIRMED", "CANCELLED").
     */
    private String status;
    /**
     * The date and time when the booking was made.
     */
    private LocalDateTime bookingTime;
    /**
     * The total price of the booking.
     */
    private BigDecimal totalPrice;
    /**
     * A list of DTOs containing information about the passengers included in this booking.
     * See {@link PassengerInfoDTO}.
     */
    private List<PassengerInfoDTO> passengers;

    /**
     * Static factory method to create a {@link BookingDTO} from a {@link Booking} entity.
     *
     * @param booking The {@link Booking} entity to convert.
     * @return A new {@link BookingDTO} instance populated with data from the entity, or null if the input is null.
     */
    public static BookingDTO fromBooking(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new BookingDTO(
                booking.getBookingId(),
                booking.getReference(),
                FlightDTO.fromFlight(booking.getFlight()),
                UserDTO.fromUser(booking.getUser()),
                booking.getStatus(),
                booking.getBookingTime(),
                booking.getTotalPrice(),
                booking.getPassengers() != null ? booking.getPassengers().stream()
                        .map(PassengerInfoDTO::fromPassenger)
                        .collect(Collectors.toList()) : null
        );
    }
}
