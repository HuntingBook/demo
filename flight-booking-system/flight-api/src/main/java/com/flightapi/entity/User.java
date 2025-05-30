package com.flightapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user entity in the system.
 * Stores information about users, including their credentials and personal details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER") // "USER" is a reserved keyword in some databases, consider renaming if issues arise.
public class User {

    /**
     * The unique identifier for the user.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /**
     * The email address of the user, used for login and communication.
     * This field is unique and cannot be null.
     */
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    /**
     * The hashed password for the user account.
     * This field cannot be null.
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * The first name of the user.
     * This field cannot be null.
     */
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    /**
     * The last name of the user.
     * This field cannot be null.
     */
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    /**
     * The country of residence of the user.
     * This field cannot be null.
     */
    @Column(nullable = false, length = 100)
    private String country;

    /**
     * The phone number of the user.
     * This field is optional.
     */
    @Column(length = 20)
    private String phone;
}
