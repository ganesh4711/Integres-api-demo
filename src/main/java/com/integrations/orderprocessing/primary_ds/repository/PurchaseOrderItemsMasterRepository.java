package com.integrations.orderprocessing.primary_ds.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.integrations.orderprocessing.primary_ds.entity.OrderItemMaster;

public interface PurchaseOrderItemsMasterRepository extends JpaRepository<OrderItemMaster,Long> {
	
    Optional<OrderItemMaster> findByProductIdAndPurchaseOrder_OrderNumber(String productId, String orderNumber);
    
    @Query(value = "select * from purchase_order_item_master where order_rec_id=:orderRecId", nativeQuery = true)
    Optional<List<OrderItemMaster>> findByOrderRecId(@Param("orderRecId") Number orderRecId);
    
    @Query(value = "select * from purchase_order_item_master where order_rec_id=:orderRecId and item_number=:itemNum", nativeQuery = true)
    Optional<OrderItemMaster> findByOrderRecIdAndItemNum(@Param("orderRecId") Number orderRecId, @Param("itemNum") String itemNum);
}
