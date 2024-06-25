package com.integrations.orderprocessing.secondary_ds.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.integrations.orderprocessing.secondary_ds.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
	 @Query(value = "select * from product p where p.product_name =:productName LIMIT 1",nativeQuery = true)
	 Optional<Product> getByProductName(@Param("productName") String productName);
}
