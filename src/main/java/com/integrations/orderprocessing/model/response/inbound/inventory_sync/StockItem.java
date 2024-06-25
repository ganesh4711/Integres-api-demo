package com.integrations.orderprocessing.model.response.inbound.inventory_sync;

import lombok.Data;

@Data
public class StockItem {
    private String productId;
    private String quantity;
    private String itemValuationType;
    private String plantId;
    private String storageLocation;
    private String docDate;
}
