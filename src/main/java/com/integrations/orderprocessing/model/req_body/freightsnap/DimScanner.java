package com.integrations.orderprocessing.model.req_body.freightsnap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimScanner {
  private String proNumber;
  private double weight;
  private String sendImage;
}
