package com.integrations.orderprocessing.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments.ProductAdjustmentDto;
import com.integrations.orderprocessing.model.req_body.inbound.gr.GoodsReceiptData;
import com.integrations.orderprocessing.model.req_body.inbound.shipment_confirm.ShpmntConfInData;
import com.integrations.orderprocessing.model.req_body.roserocket.RRManifestAssignEvent;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.util.List;

public class ObjectUtil {
	
	public final static ObjectMapper objectMapper  = new ObjectMapper();

	public static String convertToJSONObject(Object obj) {

		JSONObject jsonObj = null;
		JSONArray jsonArray = null;

		// Use JsonConfig to customize the conversion (if needed)
		JsonConfig jsonConfig = new JsonConfig();


		try {
			/*// Optionally, you can use a PropertyFilter to exclude certain properties
			jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			    @Override
			    public boolean apply(Object source, String name, Object value) {
			        // Exclude the 'email' property from the JSON
			        return !"email".equals(name);
			    }
			});*/

			if(obj instanceof GoodsReceiptData) {
				GoodsReceiptData mGoodsReceiptData = (GoodsReceiptData) obj;

				// Convert the Java object to JSONObject
				jsonObj = JSONObject.fromObject(mGoodsReceiptData, jsonConfig);
			} else if(obj instanceof ShpmntConfInData) {
				ShpmntConfInData mShipmentConfData = (ShpmntConfInData) obj;

				// Convert the Java object to JSONObject
				jsonObj = JSONObject.fromObject(mShipmentConfData, jsonConfig);
			} else if(obj instanceof RRManifestAssignEvent) {
				RRManifestAssignEvent mManifestRequestDTO = (RRManifestAssignEvent) obj;

				// Convert the Java object to JSONObject
				jsonObj = JSONObject.fromObject(mManifestRequestDTO, jsonConfig);
			} else if (obj instanceof List<?> adjustmentDto) {

				// Convert the Java List object to Json Array
				jsonArray = JSONArray.fromObject(adjustmentDto,jsonConfig);
				return jsonArray.toString();
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}

		return jsonObj.toString();
	}
}
