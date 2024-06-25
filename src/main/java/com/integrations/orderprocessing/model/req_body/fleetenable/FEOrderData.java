package com.integrations.orderprocessing.model.req_body.fleetenable;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FEOrderData {

    @NotBlank(message = "event should not be blank")
	private String event;

    @Valid
    @NotNull(message = "order should not be null")
    private FEOrder order;
}
