package com.integrations.orderprocessing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.integrations.orderprocessing.constants.StringConstants;

import jakarta.annotation.PostConstruct;

@Component
public class DateUtil {
	
    @Value("${app.sharp.inventory-sync.scheduler.zone}")
    private String timeZone;
    
    public static String TIME_ZONE;
    
    @PostConstruct
    public void init() {
    	TIME_ZONE = timeZone;
    }
    
    public static Date getCurrentDate() {
    	 Date date = new Date();
     try { 
         // Set the desired time zone
         TimeZone timeZone = TimeZone.getTimeZone(TIME_ZONE);
         
         // Create a SimpleDateFormat instance and set the time zone
         SimpleDateFormat sdf = new SimpleDateFormat(StringConstants.DATE_TIME_PATTERN5);
         sdf.setTimeZone(timeZone);
         
         // Format the date according to the specified time zone
         String formattedDate = sdf.format(date);
         
         
        	 date = sdf.parse(formattedDate);
         } catch (ParseException e) {
         }
         
         return date;
    }

    public static String getCrntDTTMinReqFormat(String pattern){
    	
    	String formattedDateTime="";
		try {
			
			Date date = new Date();
			
			// Set the desired time zone
			TimeZone timeZone = TimeZone.getTimeZone(TIME_ZONE);
			
			// Create a SimpleDateFormat instance and set the time zone
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			sdf.setTimeZone(timeZone);
			
			// Format the date according to the specified time zone
			formattedDateTime = sdf.format(date);
		} catch (Exception e) {
		}

        return formattedDateTime;
    }
    
    public static String getCrntDTinReqFormat(String pattern){
        return getCrntDTTMinReqFormat(pattern);
    }
    
    public static LocalDateTime getCrntTimeStampinReqFormat(String pattern){
        
     // String representation of the date
        String dateString = getCrntDTTMinReqFormat(pattern);
        
       // Set the desired time zone
        TimeZone timeZone = TimeZone.getTimeZone(TIME_ZONE);

        // Define the formatter for the given date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        

        // Parse the string into a LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
        
        return dateTime; 
    }
    
    public static String convertToDTTMtoDTTM(String inputDateTime, String inFormat, String outFormat) {
    	String reqDTTM="";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inFormat);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outFormat);

            Date date = inputFormat.parse(inputDateTime);
            reqDTTM =  outputFormat.format(date);
        } catch (ParseException e) {
        }
        
        return reqDTTM;
    }

    public static String convertToDTTMtoTM(String inputDateTime, String inFormat, String outFormat) {
        String reqTM="";
        if(inFormat.equals(StringConstants.DATE_TIME_PATTERN8) && outFormat.equals(StringConstants.TIME_PATTERN)) {
            try {
                String time = inputDateTime.split("T")[1];
                String[] timeArr = time.split(":");

                reqTM = new StringBuilder()
                        .append(timeArr[0])
                        .append(timeArr[1])
                        .append(timeArr[2].substring(0, 2))
                        .toString();
            } catch (Exception e) {
            }
        }

        return reqTM;
    }

}
