package com.flightbooking.dto;

import com.flightbooking.entity.Passenger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerInfoDTO {

    private Long passengerId;
    private String firstName;
    private String lastName;
    private String email;

    public static PassengerInfoDTO fromPassenger(Passenger passenger) {
        if (passenger == null) {
            return null;
        }
        return new PassengerInfoDTO(
                passenger.getPassengerId(),
                passenger.getFirstName(),
                passenger.getLastName(),
                passenger.getEmail()
        );
    }
}
