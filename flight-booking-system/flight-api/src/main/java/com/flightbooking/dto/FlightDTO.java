package com.flightbooking.dto;

import com.flightbooking.entity.Flight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {

    private Long flightId;
    private String flightNumber;
    private AirportDTO departureAirport;
    private AirportDTO destinationAirport;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private BigDecimal price;

    public static FlightDTO fromFlight(Flight flight) {
        if (flight == null) {
            return null;
        }
        return new FlightDTO(
                flight.getFlightId(),
                flight.getFlightNumber(),
                AirportDTO.fromAirport(flight.getDepartureAirport()),
                AirportDTO.fromAirport(flight.getDestinationAirport()),
                flight.getDepartureDate(),
                flight.getDepartureTime(),
                flight.getPrice()
        );
    }
}
