package com.integrations.orderprocessing.primary_ds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.integrations.orderprocessing.primary_ds.entity.RRManifestAssignEventEntity;

//@Repository
public interface RRManifestAssignEventRepository
//extends JpaRepository<RRManifestAssignEventEntity, Long> 
{
	
//	@Query(value = "select count(*) from rr_manifest_assign where order_id=:orderId and source=:source", nativeQuery = true)
//	public int getManifestAssignCountByOrderIdAndSource(@Param("orderId") String orderId, @Param("source") String source);
//	
//	@Query(value = "select * from rr_manifest_assign where manifest_id=:manifestId", nativeQuery = true)
//	public RRManifestAssignEventEntity getRRManifestAssignEventByManifestId(@Param("manifestId") String manifestId);
}
