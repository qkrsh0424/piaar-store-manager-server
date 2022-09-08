package com.piaar_store_manager.server.domain.erp_return_item.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piaar_store_manager.server.domain.erp_return_item.entity.ErpReturnItemEntity;
import com.piaar_store_manager.server.domain.erp_return_item.proj.ErpReturnItemProj;
import com.piaar_store_manager.server.domain.erp_return_item.repository.ErpReturnItemCustomJdbc;
import com.piaar_store_manager.server.domain.erp_return_item.repository.ErpReturnItemRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpReturnItemService {
    private final ErpReturnItemRepository erpReturnItemRepository;
    private final ErpReturnItemCustomJdbc erpReturnItemCustomJdbc;

    public ErpReturnItemEntity searchOne(UUID id) {
        Optional<ErpReturnItemEntity> entityOpt = erpReturnItemRepository.findById(id);

        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new CustomNotFoundDataException("존재하지 않는 데이터입니다.");
        }
    }

    public void saveListAndModify(List<ErpReturnItemEntity> entities) {
        erpReturnItemRepository.saveAll(entities);
    }

    @Transactional
    public void bulkInsert(List<ErpReturnItemEntity> itemEntities){
        erpReturnItemCustomJdbc.jdbcBulkInsert(itemEntities);
    }

    public Page<ErpReturnItemProj> findAllM2OJByPage(Map<String, Object> params, Pageable pageable) {
        return erpReturnItemRepository.qfindAllM2OJByPage(params, pageable);
    }

    public List<ErpReturnItemProj> findAllM2OJByReleasedItem(List<UUID> ids, Map<String, Object> params) {
        return erpReturnItemRepository.qfindAllM2OJByReleasedItemIdList(ids, params);
    }
}
