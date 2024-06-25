package com.integrations.orderprocessing.primary_ds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.integrations.orderprocessing.primary_ds.entity.ShipmentOutMaster;


@Repository
public interface ShipmentOutMasterRepository extends JpaRepository<ShipmentOutMaster,Long> {

}