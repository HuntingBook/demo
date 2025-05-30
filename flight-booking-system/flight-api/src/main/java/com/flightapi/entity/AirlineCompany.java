package com.flightapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "airline_companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirlineCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airline_id")
    private Long airlineId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "logo_url") // URL to the airline's logo
    private String logoUrl;

    // Optional: If an airline company can have many flights
    // @OneToMany(mappedBy = "airlineCompany", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Flight> flights;
}