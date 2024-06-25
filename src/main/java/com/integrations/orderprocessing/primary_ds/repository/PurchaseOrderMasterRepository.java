package com.integrations.orderprocessing.primary_ds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integrations.orderprocessing.primary_ds.entity.PurchaseOrderMaster;

import java.util.Optional;

public interface PurchaseOrderMasterRepository extends JpaRepository<PurchaseOrderMaster,Long> {
    Optional<PurchaseOrderMaster> findByOrderNumber(String orderNumber);
}
