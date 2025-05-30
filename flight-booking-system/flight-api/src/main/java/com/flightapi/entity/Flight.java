package com.flightapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a flight entity in the system.
 * Stores information about flights, including flight number, departure and destination airports,
 * departure date and time, and price.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FLIGHT")
public class Flight {

    /**
     * The unique identifier for the flight.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Long flightId;

    /**
     * The flight number (e.g., "BA245").
     * This field cannot be null.
     */
    @Column(name = "flight_number", nullable = false, length = 10)
    private String flightNumber;

    /**
     * The departure airport for this flight.
     * This is a many-to-one relationship with the {@link Airport} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private Airport departureAirport;

    /**
     * The destination airport for this flight.
     * This is a many-to-one relationship with the {@link Airport} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_airport_id", nullable = false)
    private Airport destinationAirport;

    /**
     * The date of departure for this flight.
     * This field cannot be null.
     */
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    /**
     * The time of departure for this flight.
     * This field cannot be null.
     */
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    /**
     * The price of a ticket for this flight.
     * This field cannot be null.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
