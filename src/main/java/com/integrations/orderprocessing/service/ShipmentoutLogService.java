package com.integrations.orderprocessing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integrations.orderprocessing.primary_ds.entity.ShipmentOutMaster;
import com.integrations.orderprocessing.primary_ds.repository.ShipmentOutMasterRepository;

@Service
public class ShipmentoutLogService {


    @Autowired
    private ShipmentOutMasterRepository shipmentOutMasterRepository;

    public void saveOrderLog(ShipmentOutMaster shipmentoutLogService) {
        shipmentOutMasterRepository.save(shipmentoutLogService);
    }
}
