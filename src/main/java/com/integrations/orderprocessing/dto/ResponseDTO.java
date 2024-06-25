package com.integrations.orderprocessing.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
	private boolean success;
	private String message;
	private String code;
	private String refNum;
	private Object data; 
}
