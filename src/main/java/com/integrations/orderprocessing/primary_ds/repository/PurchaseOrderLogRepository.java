package com.integrations.orderprocessing.primary_ds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integrations.orderprocessing.primary_ds.entity.PurchaseOrderLog;


public interface PurchaseOrderLogRepository extends JpaRepository<PurchaseOrderLog,Long> {

}
