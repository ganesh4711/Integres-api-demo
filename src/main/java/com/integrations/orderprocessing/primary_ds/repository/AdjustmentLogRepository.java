package com.integrations.orderprocessing.primary_ds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integrations.orderprocessing.primary_ds.entity.AdjustmentLog;

@Repository
public interface AdjustmentLogRepository extends JpaRepository<AdjustmentLog,Long> {
}
