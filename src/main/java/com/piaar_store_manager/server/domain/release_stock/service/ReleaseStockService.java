package com.piaar_store_manager.server.domain.release_stock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.domain.release_stock.entity.ReleaseStockEntity;
import com.piaar_store_manager.server.domain.release_stock.repository.ReleaseStockCustomJdbc;
import com.piaar_store_manager.server.domain.release_stock.repository.ReleaseStockRepository;
import com.piaar_store_manager.server.service.user.UserService;

@Service
@RequiredArgsConstructor
public class ReleaseStockService {
    private final ReleaseStockRepository releaseStockRepository;
    private final ReleaseStockCustomJdbc releaseStockCustomJdbc;
    private final UserService userService;

    public void saveAndModifyList(List<ReleaseStockEntity> entities) {
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();

        releaseStockRepository.saveAll(entities);
    }

    public void bulkInsert(List<ReleaseStockEntity> entities){
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();

        releaseStockCustomJdbc.jdbcBulkInsert(entities);
    }

    public void deleteByErpOrderItemIds(List<UUID> erpOrderItemIds){
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();
        
        releaseStockRepository.deleteByErpOrderItemIds(erpOrderItemIds);
    }
}
