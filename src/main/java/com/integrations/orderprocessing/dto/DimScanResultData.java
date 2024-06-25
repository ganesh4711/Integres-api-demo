package com.integrations.orderprocessing.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimScanResultData {
	  private String id;
      private String company_id;
      private String pro_number;
      private String ship_date;
      private String pallet_time;
      private String pallet_sequence;
      private String MeasureFlag;
      private String volume;
      private String height;
      private String length;
      private String width;
      private String cubic_feet;
      private String weight;
      private int image_count;
      private String edims;
      private String copies;
      private String scanned_terminal_id;
      private String scanner_id;
      private String uofm_dims;
      private String uofm_weight;
      private String tms_id;
      private List<String> images_urls;
      private List<String> answers;
      private String scan_date;
      private String scan_time;
      private String scan_sequence;
      private String location_id;
      private String dimensioner_id;
      private String scan_Date_Time;
}
