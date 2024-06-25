package com.integrations.orderprocessing.util;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileUtil {
	
	public static Object readJsonFile() {
        // Specify the path to the JSON file in the source folder
        String filePath = "C:\\Users\\Developper\\Documents\\Project_Docs\\Testing\\sample.json";
        Object obj = null;
        try {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Read JSON file into JsonNode
            JsonNode jsonNode = objectMapper.readTree(new File(filePath));

            // Access data from JsonNode
            String name = jsonNode.get("name").asText();
            int age = jsonNode.get("age").asInt();
            String city = jsonNode.get("city").asText();

            // Print the data
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("City: " + city);

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return obj;
    }
	
//	public static void main(String[] args) {
//		readJsonFile();
//	}
}
