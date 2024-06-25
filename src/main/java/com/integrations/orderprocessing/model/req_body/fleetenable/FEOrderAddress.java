package com.integrations.orderprocessing.model.req_body.fleetenable;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEOrderAddress {

    @NotNull(message = "addressBookId should not be null")
	private String addressBookId;

    @NotNull(message = "addressBookExternalId should not be null")
    private String addressBookExternalId;

    @NotBlank(message = "orgName should not be blank")
    private String orgName;

    @NotNull(message = "contactName should not be null")
    private String contactName;

    @NotBlank(message = "address1 should not be blank")
    private String address1;

    @NotNull(message = "address2 should not be null")
    private String address2;

    @NotNull(message = "suite should not null")
    private String suite;

    @NotNull(message = "city should not be null")
    private String city;

    @NotNull(message = "state should not be null")
    private String state;

    @NotNull(message = "country should not be null")
    private String country;

    @NotNull(message = "postal should not be null")
    private String postal;

    @NotNull(message = "phone should not be null")
    private String phone;

    @NotNull(message = "phoneExt should not be null")
    private String phoneExt;

    @NotNull(message = "email should not be null")
    private String email;

    @NotNull(message = "fax should not be null")
    private String fax;

    private double latitude;

    private double longitude;

    @NotNull(message = "requestedDate should not be null")
    private String requestedDate; // Newly Added
}
