package com.integrations.orderprocessing.model.response.inbound.inventory_sync;

import java.util.List;

import lombok.Data;

@Data
public class StockInWarehouse {
	private String docNum;
	private List<StockItem> stockItems;
}
