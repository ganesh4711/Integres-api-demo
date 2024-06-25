package com.integrations.orderprocessing.secondary_ds.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.integrations.orderprocessing.secondary_ds.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Long> {
	  @Query(value = "select * from organizations o where o.id=:organizationId LIMIT 1",nativeQuery = true)
	  Optional<Organization> fetchOrganizationById(@Param("organizationId") long organizationId);
	  
	  @Query(value = "select * from organizations o where UPPER(o.name) = UPPER(:organizationName) LIMIT 1",nativeQuery = true)
	  Optional<Organization> fetchOrganizationByName(@Param("organizationName") String organizationName);
}
