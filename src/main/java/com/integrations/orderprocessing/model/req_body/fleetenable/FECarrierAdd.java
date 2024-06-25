package com.integrations.orderprocessing.model.req_body.fleetenable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FECarrierAdd extends FEOrgAdd {

	@Valid
	@NotNull(message = "shippers should not be null")
	private List<FEShipperSetup> shippers;
}
