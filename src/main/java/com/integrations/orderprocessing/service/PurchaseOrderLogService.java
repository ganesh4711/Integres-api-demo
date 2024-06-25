package com.integrations.orderprocessing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.primary_ds.entity.PurchaseOrderLog;
import com.integrations.orderprocessing.primary_ds.repository.PurchaseOrderLogRepository;

@Service
public class PurchaseOrderLogService {

    @Autowired
    private PurchaseOrderLogRepository purchaseOrderLogRepository;


    public void saveOrderLog(PurchaseOrderLog orderLog) {
            purchaseOrderLogRepository.save(orderLog);
    }
}
