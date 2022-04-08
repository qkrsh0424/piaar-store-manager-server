package com.piaar_store_manager.server.model.option_package.repository;

import com.piaar_store_manager.server.model.option_package.entity.OptionPackageEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionPackageRepository extends JpaRepository<OptionPackageEntity, Integer> {
    
}
