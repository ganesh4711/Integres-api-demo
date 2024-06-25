package com.integrations.orderprocessing.model.req_body.fleetenable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEAccountCreatedData {

	@Valid
	@NotNull(message = "carrier should not be null")
	private FECarrierAdd carrier;
}
