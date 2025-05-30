package com.flightapi.repository;

import com.flightapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link User} entities.
 * Provides CRUD operations and custom query methods for accessing user data.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return an {@link Optional} containing the found user, or {@link Optional#empty()} if no user with the given email exists.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists with the given email address.
     *
     * @param email The email address to check.
     * @return {@code true} if a user with the given email exists, {@code false} otherwise.
     */
    Boolean existsByEmail(String email);
}
