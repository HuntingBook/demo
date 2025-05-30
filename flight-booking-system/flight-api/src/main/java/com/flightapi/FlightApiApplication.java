package com.flightapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Flight Booking API.
 * This class initializes and runs the Spring Boot application.
 */
@SpringBootApplication
public class FlightApiApplication {

    /**
     * The main entry point for the Spring Boot application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(FlightApiApplication.class, args);
    }

}