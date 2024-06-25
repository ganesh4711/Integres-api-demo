package com.integrations.orderprocessing.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SystemUtil {
  public static String getSystemIP() {
	  String ip="";
	  try {
          InetAddress localhost = InetAddress.getLocalHost();
          ip= localhost.getHostAddress();
      } catch (UnknownHostException e) {
          e.printStackTrace();
      }
	  return ip;
  }
  
//  public static void main(String[] args) {
//	  System.out.println("System IP Address : " + getSystemIP());
//}
}
