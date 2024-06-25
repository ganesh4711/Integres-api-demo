package com.integrations.orderprocessing.model.req_body.stackenable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InwardRootStackEnable {
	private List<InwardChildStackEnable> orders;
}
