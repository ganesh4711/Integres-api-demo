package com.integrations.orderprocessing.model.req_body.fleetenable;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEAccountDeletedData {
	@Valid
	@NotNull(message = "carrier should not be null")
	private FECarrierDelete carrier;
}
