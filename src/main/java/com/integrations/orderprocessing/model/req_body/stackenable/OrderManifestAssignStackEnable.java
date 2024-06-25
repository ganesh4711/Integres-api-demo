package com.integrations.orderprocessing.model.req_body.stackenable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderManifestAssignStackEnable {
	private String eventName="";
	private String manifestId="";
	private String timestamp="";
	private String orderId="";
	private String source=""; 
	private String customerId="";
}
