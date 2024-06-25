package com.integrations.orderprocessing.primary_ds.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.integrations.orderprocessing.primary_ds.entity.GoodsReceiptLog;

@Repository
public interface GoodsReceiptLogRepository extends JpaRepository<GoodsReceiptLog,Long> {
	
	@Query(value = "select * from goods_receipt_log where order_number=:orderNum and item_number=:itemNum", nativeQuery = true)
	public Optional<List<GoodsReceiptLog>> getGoodsReceiptLogList(@Param("orderNum") String orderNum, @Param("itemNum") String itemNum);
	
	@Query(value = "select * from goods_receipt_log where gr_rec_id=:grRecId", nativeQuery = true)
	public Optional<GoodsReceiptLog> getGoodsReceiptLogBygrRecId(@Param("grRecId") Long grRecId);
	
	@Transactional
	@Modifying
	@Query(value = "update goods_receipt_log set file_name=:fileName, status=:flag where gr_rec_id=:grRecId", nativeQuery = true)
	public int updateGoodsReceiptLogBygrRecId(@Param("grRecId") Long grRecId, @Param("fileName") String fileName, @Param("flag") Boolean flag);
}
