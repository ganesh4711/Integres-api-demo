package com.integrations.orderprocessing.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

public class RRTokenSharpPayload extends RRTokenPayload{
	public RRTokenSharpPayload(String grant_type, String client_id, String client_secret, String username,
			String password) {
		super(grant_type, client_id, client_secret, username, password);
	}
}
