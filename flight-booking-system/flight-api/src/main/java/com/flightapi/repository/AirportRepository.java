package com.flightapi.repository;

import com.flightapi.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Airport} entities.
 * Provides CRUD operations and custom query methods for accessing airport data.
 */
@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {

    /**
     * Finds an airport by its IATA code.
     *
     * @param code The IATA code of the airport to find.
     * @return an {@link Optional} containing the found airport, or {@link Optional#empty()} if no airport with the given code exists.
     */
    Optional<Airport> findByCode(String code);
}
