package com.piaar_store_manager.server.domain.erp_release_complete_header.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_release_complete_header.entity.ErpReleaseCompleteHeaderEntity;
import com.piaar_store_manager.server.domain.erp_release_complete_header.repository.ErpReleaseCompleteHeaderRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpReleaseCompleteHeaderService {
    private final ErpReleaseCompleteHeaderRepository erpReleaseCompleteHeaderRepository;

    /**
     * <b>DB Insert Or Update Related Method</b>
     * <p>
     * 피아르 엑셀 헤더를 저장 or 수정한다.
     * 
     * @param entity : ErpReleaseCompleteHeaderEntity
     * @see ErpReleaseCompleteHeaderRepository#save
     */
    public void saveAndModify(ErpReleaseCompleteHeaderEntity entity) {
        erpReleaseCompleteHeaderRepository.save(entity);
    }

     /**
     * <b>DB Select Related Method</b>
     * <p>
     * erp release complete header을 조회한다.
     *
     * @return List::ErpReleaseCompleteHeaderEntity::
     * @see ErpReleaseCompleteHeaderRepository#findAll
     */
    public List<ErpReleaseCompleteHeaderEntity> findAll() {
        return erpReleaseCompleteHeaderRepository.findAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * erp release complete header을 조회한다.
     * 
     * @param id : UUID
     * @return ErpReleaseCompleteHeaderEntity
     */
    public ErpReleaseCompleteHeaderEntity searchOne(UUID id) {
        Optional<ErpReleaseCompleteHeaderEntity> entityOpt = erpReleaseCompleteHeaderRepository.findById(id);

        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new CustomNotFoundDataException("존재하지 않는 데이터입니다.");
        }
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * erp release complete header을 제거한다.
     * 
     * @param entity : ErpReleaseCompleteHeaderEntity
     */
    public void deleteOne(ErpReleaseCompleteHeaderEntity entity) {
        erpReleaseCompleteHeaderRepository.delete(entity);
    }
}
