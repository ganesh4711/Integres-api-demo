package com.integrations.orderprocessing.primary_ds.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.integrations.orderprocessing.primary_ds.entity.ShipmentConfirmLog;

@Repository
public interface ShipmentConfirmLogRepository extends JpaRepository<ShipmentConfirmLog, Long>{
	
	@Query(value = "select * from shipment_confirm_log where shipment_number=:shipmentNum and delivery_number=:deliveryNum", nativeQuery = true)
	public Optional<ShipmentConfirmLog> getShipmentConfirmLog(@Param("shipmentNum") String shipmentNum, @Param("deliveryNum") String deliveryNum);
	
	@Transactional
	@Modifying
	@Query(value = "update shipment_confirm_log set file_name=:fileName, status=:flag where sc_rec_id=:scRecId", nativeQuery = true)
	public int updateShipmentConfirmLog(@Param("scRecId") Long scRecId, @Param("fileName") String fileName, @Param("flag") Boolean flag);
}
