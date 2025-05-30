package com.flightapi.repository;

import com.flightapi.entity.Booking;
import com.flightapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Keep this if you still have the non-paginated version for other uses
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Booking} entities.
 * Provides CRUD operations and custom query methods for accessing booking data, including paginated results.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Finds all bookings made by a specific user, ordered by booking time in descending order, with pagination.
     *
     * @param user     The user whose bookings are to be retrieved.
     * @param pageable Pagination information (page number, size, sort).
     * @return A {@link Page} of bookings for the given user, ordered by booking time descending.
     */
    Page<Booking> findByUserOrderByBookingTimeDesc(User user, Pageable pageable);

    /**
     * Finds all bookings made by a specific user with a specific status, ordered by booking time in descending order, with pagination.
     *
     * @param user     The user whose bookings are to be retrieved.
     * @param status   The status of the bookings to filter by (e.g., "CONFIRMED", "CANCELLED").
     * @param pageable Pagination information (page number, size, sort).
     * @return A {@link Page} of bookings for the given user and status, ordered by booking time descending.
     */
    Page<Booking> findByUserAndStatusOrderByBookingTimeDesc(User user, String status, Pageable pageable);

    /**
     * Finds a booking by its unique reference code.
     *
     * @param reference The reference code of the booking to find.
     * @return an {@link Optional} containing the found booking, or {@link Optional#empty()} if no booking with the given reference exists.
     */
    Optional<Booking> findByReference(String reference);

    /**
     * Finds all bookings made by a specific user, ordered by booking time in descending order.
     * Note: This method returns a list and is not paginated. Consider using the paginated version
     * {@link #findByUserOrderByBookingTimeDesc(User, Pageable)} for large datasets.
     *
     * @param user The user whose bookings are to be retrieved.
     * @return A list of bookings for the given user, ordered by booking time descending.
     */
    List<Booking> findByUserOrderByBookingTimeDesc(User user);
}
