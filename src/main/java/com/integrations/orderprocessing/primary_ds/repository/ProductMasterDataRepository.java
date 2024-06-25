package com.integrations.orderprocessing.primary_ds.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.integrations.orderprocessing.primary_ds.entity.ProductMasterData;

@Repository
public interface ProductMasterDataRepository extends JpaRepository<ProductMasterData, String>{
	
	public static final String schema="public";
	
	@Query(value = "select * from product_master_data where product_id=:prodId", nativeQuery = true)
	public Optional<ProductMasterData> getProductMasterDataById(@Param("prodId") String prodId);
}
