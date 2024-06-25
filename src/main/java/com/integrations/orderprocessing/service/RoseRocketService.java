package com.integrations.orderprocessing.service;

import java.util.List;

import com.integrations.orderprocessing.model.req_body.roserocket.RRManifestAssignEvent;
import com.integrations.orderprocessing.primary_ds.entity.RRManifestAssignEventEntity;
import com.integrations.orderprocessing.secondary_ds.entity.EventLog;

import net.sf.json.JSONObject;

public interface RoseRocketService {
	public EventLog onSaveManifestAssignEvent(RRManifestAssignEvent rrManifestAssignEvent,
			String orderId, String source, String flowRefNum);

	public String getRRToken(String rrCustomerType, String flowRefNum);

	public String getOrderIdByManifestId(String rrCustomerType, String manifestId, String flowRefNum, String token);

	public String getPartnerCarrierIdByManifestId(String rrCustomerType, String manifestId, String flowRefNum, String token);

	public JSONObject getPartnerCarrierDataByPartnerCarrierId(String rrCustomerType, String partnerCarrierId, String flowRefNum, String token);

	public List<String> getAccessorialNamesByAccessorialIds(String rrCustomerType, List<String> accessorialIds, String flowRefNum, String token);
	
	public JSONObject getOrderDataByOrderId(String rrCustomerType, String orderId, String flowRefNum, String token);
	
	public String getCustomerIdByCustomerType(String type);
	
	public String getCustomerTypeByCustomerId(JSONObject cutomerObj);
}
