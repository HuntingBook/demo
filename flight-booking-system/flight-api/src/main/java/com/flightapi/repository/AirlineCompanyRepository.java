package com.flightapi.repository;

import com.flightapi.entity.AirlineCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirlineCompanyRepository extends JpaRepository<AirlineCompany, Long> {
    Optional<AirlineCompany> findByName(String name);
}