package com.integrations.orderprocessing.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

public class RoseRocketUtil {
  public static List<String> getAccessorialsList(JSONObject orderData){
	  List<String> accessorialsList = new ArrayList<String>();
	  
	  try {
		 orderData =  orderData.getJSONObject("order");
		 accessorialsList = (List<String>)orderData.get("accessorials");
	  }catch(Exception e) {
		  
	  }
	  
	  return accessorialsList;
  }
}
