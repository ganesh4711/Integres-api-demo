package com.integrations.orderprocessing.util;

import java.security.SecureRandom;

public class ShipperUtil {
	
    public static String generateBolNumber() {
        // Define characters that can be used in the BOL number
        //String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    	String characters = "0123456789";

        // Define the desired length of the BOL number
        int bolLength = 17;

        // Use SecureRandom to generate a random BOL number
        SecureRandom random = new SecureRandom();
        StringBuilder bolNumberBuilder = new StringBuilder();

        for (int i = 0; i < bolLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            bolNumberBuilder.append(randomChar);
        }

        return bolNumberBuilder.toString();
    }
    
    public static String generateSerialNumber() {
        // Define characters that can be used in the BOL number
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        // Define the desired length of the BOL number
        int bolLength = 17;

        // Use SecureRandom to generate a random BOL number
        SecureRandom random = new SecureRandom();
        StringBuilder bolNumberBuilder = new StringBuilder();

        for (int i = 0; i < bolLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            bolNumberBuilder.append(randomChar);
        }

        return bolNumberBuilder.toString();
    }
    
    public static String generateSealNumber() {
        // Define characters that can be used in the BOL number
        //String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    	String characters = "0123456789";

        // Define the desired length of the BOL number
        int bolLength = 10;

        // Use SecureRandom to generate a random BOL number
        SecureRandom random = new SecureRandom();
        StringBuilder bolNumberBuilder = new StringBuilder();

        for (int i = 0; i < bolLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            bolNumberBuilder.append(randomChar);
        }

        return bolNumberBuilder.toString();
    }
    
    public static String generateCartonLblId() {
        // Define characters that can be used in the BOL number
        //String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    	String characters = "0123456789";

        // Define the desired length of the BOL number
        int bolLength = 20;

        // Use SecureRandom to generate a random BOL number
        SecureRandom random = new SecureRandom();
        StringBuilder bolNumberBuilder = new StringBuilder();

        for (int i = 0; i < bolLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            bolNumberBuilder.append(randomChar);
        }

        return bolNumberBuilder.toString();
    }
    
//    public static void main(String[] args) {
//        String num = generateCartonLblId();
//        System.out.println("Generated Carton Label Id: " + num);
//    }
}
