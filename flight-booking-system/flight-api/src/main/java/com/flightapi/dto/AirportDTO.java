package com.flightapi.dto;

import com.flightapi.entity.Airport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportDTO {

    private Long airportId;
    private String code;
    private String name;
    private String city;

    public static AirportDTO fromAirport(Airport airport) {
        if (airport == null) {
            return null;
        }
        return new AirportDTO(
                airport.getAirportId(),
                airport.getCode(),
                airport.getName(),
                airport.getCity()
        );
    }
}
