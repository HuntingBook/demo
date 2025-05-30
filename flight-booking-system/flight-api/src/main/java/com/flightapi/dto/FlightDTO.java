package com.flightapi.dto;

import com.flightapi.entity.Flight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object (DTO) for representing flight details.
 * This DTO is used to transfer flight information between layers and in API responses,
 * for example, when searching for flights or displaying booking details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {

    /**
     * The unique identifier for the flight.
     */
    private Long flightId;
    /**
     * The flight number (e.g., "AA123").
     */
    private String flightNumber;
    /**
     * DTO containing details of the departure airport.
     * See {@link AirportDTO}.
     */
    private AirportDTO departureAirport;
    /**
     * DTO containing details of the destination airport.
     * See {@link AirportDTO}.
     */
    private AirportDTO destinationAirport;
    /**
     * The date of departure.
     */
    private LocalDate departureDate;
    /**
     * The time of departure.
     */
    private LocalTime departureTime;
    /**
     * The price of the flight ticket.
     */
    private BigDecimal price;

    /**
     * Static factory method to create a {@link FlightDTO} from a {@link Flight} entity.
     *
     * @param flight The {@link Flight} entity to convert.
     * @return A new {@link FlightDTO} instance populated with data from the entity, or null if the input is null.
     */
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
