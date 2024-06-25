package com.integrations.orderprocessing.util;

public class StringUtil {
	
	public static int nthIndexOf(String input, String substring, int nth) {
	    if (nth == 1) {
	        return input.indexOf(substring);
	    } else {
	        return input.indexOf(substring, nthIndexOf(input, substring, nth - 1) + substring.length());
	    }
	}
}
