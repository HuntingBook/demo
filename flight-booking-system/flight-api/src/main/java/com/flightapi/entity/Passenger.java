package com.flightapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a passenger entity in the system.
 * Stores information about a passenger, including their name, email, and the booking they are associated with.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PASSENGER")
public class Passenger {

    /**
     * The unique identifier for the passenger.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passenger_id")
    private Long passengerId;

    /**
     * The first name of the passenger.
     * This field cannot be null.
     */
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    /**
     * The last name of the passenger.
     * This field cannot be null.
     */
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    /**
     * The email address of the passenger.
     * This field cannot be null.
     */
    @Column(nullable = false, length = 100)
    private String email;

    /**
     * The booking to which this passenger belongs.
     * This is a many-to-one relationship with the {@link Booking} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
}
