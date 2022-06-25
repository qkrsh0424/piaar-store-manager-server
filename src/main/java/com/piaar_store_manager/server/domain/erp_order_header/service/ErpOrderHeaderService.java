package com.piaar_store_manager.server.domain.erp_order_header.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_order_header.entity.ErpOrderHeaderEntity;
import com.piaar_store_manager.server.domain.erp_order_header.repository.ErpOrderHeaderRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpOrderHeaderService {
    private final ErpOrderHeaderRepository erpOrderHeaderRepository;

    /**
     * <b>DB Insert Or Update Related Method</b>
     * <p>
     * 피아르 엑셀 헤더를 저장 or 수정한다.
     * 
     * @param entity : ErpOrderHeaderEntity
     * @see ErpOrderHeaderRepository#save
     */
    public void saveAndModify(ErpOrderHeaderEntity entity) {
        erpOrderHeaderRepository.save(entity);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * erp order header을 조회한다.
     *
     * @return List::ErpOrderHeaderEntity::
     * @see ErpOrderHeaderRepository#findAll
     */
    public List<ErpOrderHeaderEntity> findAll() {
        return erpOrderHeaderRepository.findAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * erp order header을 조회한다.
     * 
     * @param id : UUID
     * @return ErpOrderHeaderEntity
     */
    public ErpOrderHeaderEntity searchOne(UUID id) {
        Optional<ErpOrderHeaderEntity> entityOpt = erpOrderHeaderRepository.findById(id);

        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new CustomNotFoundDataException("존재하지 않는 데이터입니다.");
        }
    }

    public ErpOrderHeaderEntity findOne(UUID id) {
        Optional<ErpOrderHeaderEntity> entityOpt = erpOrderHeaderRepository.findById(id);
        
        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            return null;
        }
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * erp order header을 제거한다.
     * 
     * @param entity : ErpOrderHeaderEntity
     */
    public void deleteOne(ErpOrderHeaderEntity entity) {
        erpOrderHeaderRepository.delete(entity);
    }
}
