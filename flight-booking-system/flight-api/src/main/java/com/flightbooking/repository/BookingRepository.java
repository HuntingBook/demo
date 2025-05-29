package com.flightbooking.repository;

import com.flightbooking.entity.Booking;
import com.flightbooking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Keep this if you still have the non-paginated version for other uses
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // For fetching bookings by user, ordered by booking time (paginated)
    Page<Booking> findByUserOrderByBookingTimeDesc(User user, Pageable pageable);

    // For fetching bookings by user and status, ordered by booking time (paginated)
    Page<Booking> findByUserAndStatusOrderByBookingTimeDesc(User user, String status, Pageable pageable);

    // For fetching a specific booking by its reference
    Optional<Booking> findByReference(String reference);

    // If you still need a non-paginated version for some specific use case, you can keep it.
    // Otherwise, if all user booking lists are paginated, this can be removed or commented out.
    List<Booking> findByUserOrderByBookingTimeDesc(User user);
}
