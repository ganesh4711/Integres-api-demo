package com.integrations.orderprocessing.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimScanResultDTO {
	private List<DimScanResultData> result; 
	private List<String> error;
}
