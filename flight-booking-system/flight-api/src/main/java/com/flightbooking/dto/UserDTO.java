package com.flightbooking.dto;

import com.flightbooking.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String country;
    private String phone;

    public static UserDTO fromUser(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getUserId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCountry(),
                user.getPhone()
        );
    }
}
