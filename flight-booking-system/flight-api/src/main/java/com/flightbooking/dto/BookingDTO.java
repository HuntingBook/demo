package com.flightbooking.dto;

import com.flightbooking.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private Long bookingId;
    private String reference;
    private FlightDTO flightInfo;
    private UserDTO userInfo;
    private String status;
    private LocalDateTime bookingTime;
    private BigDecimal totalPrice;
    private List<PassengerInfoDTO> passengers;

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
