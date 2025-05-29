package com.flightbooking.repository;

import com.flightbooking.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    // Basic CRUD methods are inherited from JpaRepository
    // Add custom query methods if needed in the future
}
