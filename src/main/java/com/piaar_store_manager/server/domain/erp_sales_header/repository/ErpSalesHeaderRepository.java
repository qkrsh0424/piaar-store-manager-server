package com.piaar_store_manager.server.domain.erp_sales_header.repository;


import com.piaar_store_manager.server.domain.erp_sales_header.entity.ErpSalesHeaderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpSalesHeaderRepository extends JpaRepository<ErpSalesHeaderEntity, Integer> {

}
