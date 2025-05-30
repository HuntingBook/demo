package com.flightapi.dto;

import com.flightapi.entity.AirlineCompany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirlineCompanyDTO {
    private Long airlineId;
    private String name;
    private String logoUrl;

    public static AirlineCompanyDTO fromAirlineCompany(AirlineCompany airlineCompany) {
        if (airlineCompany == null) {
            return null;
        }
        return new AirlineCompanyDTO(
                airlineCompany.getAirlineId(),
                airlineCompany.getName(),
                airlineCompany.getLogoUrl()
        );
    }
}