package com.piaar_store_manager.server.domain.erp_delivery_header.repository;

import com.piaar_store_manager.server.domain.erp_delivery_header.entity.ErpDeliveryHeaderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpDeliveryHeaderRepository extends JpaRepository<ErpDeliveryHeaderEntity, Integer> {

}
