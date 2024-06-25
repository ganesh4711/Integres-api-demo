package com.integrations.orderprocessing.constants;

import java.util.HashMap;
import java.util.Map;

public class FleetEnableConstants {

	public static final String FE_ORG_TYPE_CARRIER = "CARRIER";

	public static final String FE_SOURCE_NAME = "FleetEnable";
	public static final String FE_ORDER_TYPE_SIGLE_DAY_DELIVERY = "TD";
	public static final String FE_ORDER_TYPE_SINGLE_DAY_RETURN = "TR";
	public static final String FE_ORDER_TYPE_MULTIDAY_DELIVERY = "D";
	public static final String FE_ORDER_TYPE_MULTIDAY_RETURN = "R";
	public static final String FE_ORDER_TYPE_MULTIDAY_PICKUP = "P";
	public static final String FE_ORDER_TYPE_TRANSFER = "T";
	public static final String FE_ORDER_TYPE_SINGLE_DAY_MOVE = "SD";
	public static final String FE_ORDER_TYPE_MULTIDAY_DAY_MOVE = "MD";

	public static final String FE_ORDER_STATUS_NEW = "NEW";
	public static final String FE_ORDER_STATUS_PENDING = "PENDING";
	public static final String FE_ORDER_STATUS_RECEIVED = "RECEIVED";
	public static final String FE_ORDER_STATUS_ONHOLD = "ONHOLD";
	public static final String FE_ORDER_STATUS_VERIFIED = "VERIFIED";
	public static final String FE_ORDER_STATUS_ASSIGNED = "ASSIGNED";
	public static final String FE_ORDER_STATUS_DISPATCHED = "DISPATCHED";
	public static final String FE_ORDER_STATUS_COMPLETED = "COMPLETED";

	public static final String FE_ORDER_STATUS_PREPARE = "PREPARE";

	public static final String FE_ORDER_STOP_TYPE_PICKUP="PICKUP";

	public static final String FE_ORDER_STOP_TYPE_DELIVERY="DELIVERY";

	@SuppressWarnings("serial")
	public static final Map<String, String> FE_TO_SE_ORDER_STATUS_MAPPER = new HashMap<>() {
		{
			put(null, StringConstants.EMPTY_STRING);
			put(FE_ORDER_STATUS_NEW, FE_ORDER_STATUS_NEW);
			put(FE_ORDER_STATUS_PENDING, FE_ORDER_STATUS_PENDING);
			put(FE_ORDER_STATUS_RECEIVED, FE_ORDER_STATUS_RECEIVED);
			put(FE_ORDER_STATUS_ONHOLD, FE_ORDER_STATUS_ONHOLD);
			put(FE_ORDER_STATUS_VERIFIED, FE_ORDER_STATUS_VERIFIED);
			put(FE_ORDER_STATUS_ASSIGNED, FE_ORDER_STATUS_ASSIGNED);
			put(FE_ORDER_STATUS_DISPATCHED, FE_ORDER_STATUS_DISPATCHED);
			put(FE_ORDER_STATUS_COMPLETED, FE_ORDER_STATUS_COMPLETED);
			put(FE_ORDER_STATUS_PREPARE, FE_ORDER_STATUS_PREPARE);
		}
	};


}
