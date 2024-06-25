package com.integrations.orderprocessing.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCredentials {
	
	private String username;

	private String password;
}
