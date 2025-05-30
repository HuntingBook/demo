package com.flightapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an airport entity in the system.
 * Stores information about airports, including their unique ID, IATA code, name, and city.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AIRPORT")
public class Airport {

    /**
     * The unique identifier for the airport.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airport_id")
    private Long airportId;

    /**
     * The IATA code of the airport (e.g., "JFK", "LAX").
     * This code is unique and cannot be null.
     */
    @Column(unique = true, nullable = false, length = 10)
    private String code;

    /**
     * The official name of the airport.
     * This field cannot be null.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * The city where the airport is located.
     * This field cannot be null.
     */
    @Column(nullable = false, length = 100)
    private String city;
}
