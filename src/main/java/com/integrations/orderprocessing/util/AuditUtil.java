package com.integrations.orderprocessing.util;

public class AuditUtil {
	 public static String getRefNum() {
	        // Get current time in milliseconds
	        long currentTimeMillis = System.currentTimeMillis();

	        // Convert to string and pad with leading zeros if needed
	        String currentTimeString = String.format("%015d", currentTimeMillis);

	        return currentTimeString;
	    }
}
