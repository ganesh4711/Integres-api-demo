package com.integrations.orderprocessing.model.req_body.fleetenable;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEOrganization extends FEOrgAdd{

    @NotNull(message = "email should not be null")
    private String email;

    @NotNull(message = "email should not be null")
    private String phone_number;

    @NotNull(message = "fax_number should not be null")
    private String fax_number;

    @NotBlank(message = "organization_type should not be blank")
    private String organization_type;

    //@Valid
    @NotNull(message = "address should not be null")
    private FEOrgAddress address;

    @NotNull(message = "description should not be null")
    private String description;

    @NotNull(message = "website should not be null")
    private String website;

    @NotNull(message = "created_at should not be null")
    private String created_at;

    @NotNull(message = "updated_at should not be null")
    private String updated_at;
    
    @Hidden
    private String organization_sub_type;
}
