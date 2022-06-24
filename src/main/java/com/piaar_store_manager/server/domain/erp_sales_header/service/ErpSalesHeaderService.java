package com.piaar_store_manager.server.domain.erp_sales_header.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_sales_header.entity.ErpSalesHeaderEntity;
import com.piaar_store_manager.server.domain.erp_sales_header.repository.ErpSalesHeaderRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpSalesHeaderService {
    private final ErpSalesHeaderRepository erpSalesHeaderRepository;

    /**
     * <b>DB Insert Or Update Related Method</b>
     * <p>
     * 피아르 엑셀 판매 헤더를 저장 or 수정한다.
     * 
     * @param entity : ErpSalesHeaderEntity
     * @see ErpSalesHeaderRepository#save
     */
    public void saveAndModify(ErpSalesHeaderEntity entity) {
        erpSalesHeaderRepository.save(entity);
    }

     /**
     * <b>DB Select Related Method</b>
     * <p>
     * erp sales header을 모두 조회한다.
     *
     * @return List::ErpSalesHeaderEntity::
     * @see ErpSalesHeaderRepository#findAll
     */
    public List<ErpSalesHeaderEntity> findAll() {
        return erpSalesHeaderRepository.findAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * erp sales header을 조회한다.
     * 
     * @param id : UUID
     * @return ErpSalesHeaderEntity
     */
    public ErpSalesHeaderEntity searchOne(UUID id) {
        Optional<ErpSalesHeaderEntity> entityOpt = erpSalesHeaderRepository.findById(id);

        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new CustomNotFoundDataException("존재하지 않는 데이터입니다.");
        }
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * erp sales header를 제거한다.
     * 
     * @param entity : ErpSalesHeaderEntity
     */
    public void deleteOne(ErpSalesHeaderEntity entity) {
        erpSalesHeaderRepository.delete(entity);
    }
}
