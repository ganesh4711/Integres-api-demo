package com.integrations.orderprocessing.model.req_body.freightsnap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimScannerDateTime {
  private String startDate;
  private String startTime;
  private String endDate;
  private String endTime;
}
