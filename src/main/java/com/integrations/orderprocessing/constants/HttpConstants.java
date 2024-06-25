package com.integrations.orderprocessing.constants;

public class HttpConstants {
	public static final String BASE_URL = "http://localhost:8088/api/";
	public static final String GOODS_RECEIPT_URL = BASE_URL + "sendGoodsReceipt";
	public static final String SHIPMENT_CONFIRM_URL = BASE_URL + "sendShipmentConfirm";
	public static final String INVENTORY_SYNC_URL = BASE_URL + "sendInventorySync";
	
	/* =================== Http Request Types ============= */
	public static final String REQ_TYPE_GET = "GET";
	public static final String REQ_TYPE_POST = "POST";
	
	/* =================== Authorization Types ============= */
	public static final String AUTH_TYPE_BASIC_AUTH = "BASIC_AUTH";
	public static final String AUTH_TYPE_BEARER_TOKEN = "BEARER_TOKEN";
	
	/* =================== Http Response Codes ============= */
	public static final String SUCCESS_CODE = "00";
	public static final String FAILURE_CODE = "99";
}
