package com.integrations.orderprocessing.model.payload;

public class RRTokenBotristaPayload extends RRTokenPayload {
	public RRTokenBotristaPayload(String grant_type, String client_id, String client_secret, String username,
			String password) {
		super(grant_type, client_id, client_secret, username, password);
	}
}
