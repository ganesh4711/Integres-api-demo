package com.integrations.orderprocessing.model.req_body.fleetenable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEOrgAddress {

    @NotBlank(message = "address_line1 should not be null")
	private String address_line1;

    @NotNull(message = "address_line2 should not be null")
    private String address_line2;

    @NotBlank(message = "zipcode should not be null")
    private String zipcode;

    @NotBlank(message = "city should not be null")
    private String city;

    @NotBlank(message = "state should not be null")
    private String state;

    @NotBlank(message = "country should not be null")
    private String country;

    @NotNull(message = "logo should not be null")
    private String logo;

    @NotNull(message = "coordinates should not be null")
    private List<Double> coordinates;
}
