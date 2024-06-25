package com.integrations.orderprocessing.model.req_body.stackenable;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InwardChildStackEnable implements Serializable
{
	private OrderStackEnable order;
	private OrderInfoStackEnable orderInfo;
	private List<ItemDetailsStackEnable> itemDetails;
}
