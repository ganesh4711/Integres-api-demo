package com.integrations.orderprocessing.primary_ds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.integrations.orderprocessing.primary_ds.entity.ProcessedFileEntry;

@Repository
public interface ProcessedFileEntryRepository extends JpaRepository<ProcessedFileEntry, Integer>{
	
	@Query(value = "select id, file_name, processed_date, status, remarks, flow_type from processed_file_entry where file_name=:fileName", nativeQuery = true)
	public ProcessedFileEntry getEntryByFileName(String fileName); 

}
