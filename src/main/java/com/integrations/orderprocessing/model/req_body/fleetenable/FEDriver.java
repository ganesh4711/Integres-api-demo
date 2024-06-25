package com.integrations.orderprocessing.model.req_body.fleetenable;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEDriver {
	
	@NotNull(message = "driver_id should not be null")
	private String driver_pickup_address_type;
	
	@NotNull(message = "driver_id should not be null")
    private String driver_id;

    @NotNull(message = "driver_name should not be null")
    private String driver_name;

    @NotNull(message = "trailer_number should not be null")
    private String trailer_number;
}
