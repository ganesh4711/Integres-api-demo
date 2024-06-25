package com.integrations.orderprocessing.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FETokenPayload {

	private String username;

	private String password;
}
