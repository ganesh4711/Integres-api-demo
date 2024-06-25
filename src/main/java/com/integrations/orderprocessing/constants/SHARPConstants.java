package com.integrations.orderprocessing.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SHARPConstants {

	  //============= Warehouse ID's ============
	@SuppressWarnings("serial")
	public static final Map<String, String> SHARP_WH_MAP_RCV = new HashMap<>(){
		    {
		       put("CORONA", "SD01");
		       put( "NA", "");
		       put( "VALUATED", "VALUATED");
		       put( "2NDCLASS", "2NDCLASS");
		       put( "3RDCLASS", "3RDCLASS");
		       put( "4THCLASS", "4THCLASS");
		    }
	};
	   
	  //============= Purchase Order ============
	   public static final String ORDER = "ORDER";
	
	   /************** Item Constants ****************/
	   public static final String PO_ITEM_VALUATION_TYPE_QLFR = "003";
	   public static final String PO_ITEM_PO_NUM_QLFR = "030";
	   
	   /************** Product Constants ****************/
	   public static final String PO_PROD_NUM_QLFR = "001";
	   
	   /************** Item Action Codes ****************/ 
		public static final String PO_ITEM_ACTION_ADD_QLFR = "001";
		public static final String PO_ITEM_ACTION_UPDATE_QLFR = "002";
		public static final String PO_ITEM_ACTION_CANCELLED_QLFR = "003";
		public static final String PO_ITEM_ACTION_NO_UPDATE_QLFR = "004";
		public static final String PO_ITEM_ACTION_LOCKED_QLFR = "005";
	   
	   //============= Shipment ============
		public static final String ORDER_STATUS_NEW = "NEW";
		public static final String ORDER_STATUS_BOOKED = "BOOKED";
	   
	   /************** Shipment Constants ****************/
	   public static final List<String> SHPMNT_IN_TRKG_QLFRS = Arrays.asList(new String[]{"001","002","003","007"});
	   public static final String SHPMNT_IN_CONTAINER_NUM_QLFR = "001";
	   public static final String SHPMNT_IN_SEAL_NUM1_QLFR = "002";
	   public static final String SHPMNT_IN_PRO_NUM_QLFR = "003";
	   public static final String SHPMNT_IN_WMS_SHPMNTS_TRUCK_QLFR = "007";
	   
	   public static final String SHPMNT_IN_PARCEL_TYPE_QLFR = "SP";
	   public static final String SHPMNT_IN_PARCEL_TYPE_LARGE = "SFWM";
	   public static final String SHPMNT_IN_PARCEL_TYPE_SMALL = "UPSN";
	   public static final String SHPMNT_IN_PARCEL_TYPE_DFLT = "DFLT";
	   
	   public static final String SHPMNT_IN_IS_PARCEL_SMALL = "UPY";
	   
	   public static final String SHPMNT_IN_LBL_TYPE_DFLT = "UCC128";
	   
	  //============= Shipment ============

	// ==========SHARP Adjustment Constants ======================

	public static final String GM_ADJUSTMENT_701 = "701";
	public static final String GM_ADJUSTMENT_702 = "702";
	public static final String GM_ADJUSTMENT_309 = "309";
	public static final String GM_ADJUSTMENT_321 = "321";
	public static final String GM_ADJUSTMENT_322 = "322";
	public static final String GM_ADJUSTMENT_311 = "311";

	public static final String GM_SLOC_QLFR_RESV = "RESV";

	public static final String GM_TRANSACTION_CODE_MIGO = "MIGO";
	public static final String GM_TRANSACTION_CODE_MI07 = "MI07";


}
