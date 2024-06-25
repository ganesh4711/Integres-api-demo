package com.integrations.orderprocessing.service;

import java.util.List;

import net.sf.json.JSONObject;

public interface StackEnableService {

	public JSONObject onSendOrderDataToStackEnable(String shipperType, JSONObject partnerCarrierData, JSONObject orderData, List<String> accessorialNames, String flowRefNum);
}
