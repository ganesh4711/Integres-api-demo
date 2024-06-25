package com.integrations.orderprocessing.model.req_body.roserocket;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RRManifestAssignEvent {
	@NotBlank(message = "Event cannot be blank")
    private String event;
	
	@NotBlank(message = "ManifestId cannot be blank")
    private String manifest_id;
	
	@NotBlank(message = "Timestamp cannot be blank")
    private String timestamp;
}
