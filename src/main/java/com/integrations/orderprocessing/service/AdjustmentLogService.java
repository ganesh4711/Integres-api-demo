package com.integrations.orderprocessing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments.AdjustmentDetails;
import com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments.ProductAdjustment;
import com.integrations.orderprocessing.model.req_body.inbound.InventoryAdjustments.ProductAdjustmentDto;
import com.integrations.orderprocessing.primary_ds.entity.AdjustmentLog;
import com.integrations.orderprocessing.primary_ds.repository.AdjustmentLogRepository;

@Service
public class AdjustmentLogService {

    @Autowired
    AdjustmentLogRepository adjustmentLogRepository;

    public AdjustmentLog onSaveAdjustmentLogData(ProductAdjustmentDto adjData){

        AdjustmentDetails adjustmentDetails = adjData.getAdjustmentDetails();
        ProductAdjustment mfromProduct = adjustmentDetails.getFromProduct();
        ProductAdjustment mToProduct = adjustmentDetails.getToProduct();

        AdjustmentLog adjustmentLog = new AdjustmentLog();

        adjustmentLog.setCreatedDate(adjData.getAdjustmentDate());
        adjustmentLog.setNote(adjData.getNote());
        adjustmentLog.setMovementType(adjustmentDetails.getMovementType());
        adjustmentLog.setQuantity(Integer.parseInt(adjustmentDetails.getQuantity()));

        adjustmentLog.setFromProductId(mfromProduct.getProductId());
        adjustmentLog.setFromPlantId(mfromProduct.getPlantId());
        adjustmentLog.setFromStorageLocation(mfromProduct.getStorageLocation());

        if (! mfromProduct.getItemValuationType().isBlank()) {
            adjustmentLog.setFromItemValuationType(mfromProduct.getItemValuationType());
        }

        if (mToProduct != null){
            adjustmentLog.setToProductId(mToProduct.getProductId());
            adjustmentLog.setToPlantId(mToProduct.getPlantId());
            adjustmentLog.setToStorageLocation(mToProduct.getStorageLocation());
            adjustmentLog.setToItemValuationType(mToProduct.getItemValuationType());
        }

        return adjustmentLogRepository.save(adjustmentLog);
    }
}
