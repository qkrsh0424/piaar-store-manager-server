package com.piaar_store_manager.server.domain.option_package.service;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.domain.option_package.proj.OptionPackageProjection;
import com.piaar_store_manager.server.domain.option_package.repository.OptionPackageRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionPackageService {
    private final OptionPackageRepository optionPackageRepository;

    public void saveListAndModify(List<OptionPackageEntity> entities) {
        optionPackageRepository.saveAll(entities);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * parentOptionId에 대응하는 option package를 모두 조회한다. (단일 옵션에 대한 패키지 리스트 조회)
     *
     * @param parentOptionId : UUID
     * @return List::OptionPackageEntity::
     * @see OptionPackageRepository#findAllByParentOptionId
     */
     public List<OptionPackageEntity> searchListByParentOptionId(UUID parentOptionId) {
         return optionPackageRepository.findAllByParentOptionId(parentOptionId);
     }

    // [221029] NEW
    // TODO :: 제거
    public List<OptionPackageEntity> searchBatchByParentOptionId(UUID parentOptionId) {
        return optionPackageRepository.findAllByParentOptionId(parentOptionId);
    }

    // [221108] NEW2
    public List<OptionPackageProjection.RelatedProductOption> searchBatchByParentOptionId2(UUID parentOptionId) {
        return optionPackageRepository.qfindBatchByParentOptionId(parentOptionId);
    }

    /**
     * <b></b>
     * <p>
     * parentOptionIdList에 대응하는 option package를 모두 조회한다. (다중 옵션에 대한 패키지 리스트 조회)
     * 
     * @param parentOptionIdList : List::UUID::
     * @return List::OptionPackageEntity::
     * @see OptionPackageRepository#findAllByParentOptionIdList
     */
    public List<OptionPackageEntity> searchListByParentOptionIdList(List<UUID> parentOptionIdList) {
        return optionPackageRepository.findAllByParentOptionIdList(parentOptionIdList);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * parentOptionId에 대응하는 option package를 모두 제거한다.(단일 옵션에 대한 패키지 리스트 전체 제거)
     * 
     * @param parentOptionId : UUID
     * @see OptionPackageRepository#deleteBatchByParentOptionId
     */
    public void deleteBatchByParentOptionId(UUID parentOptionId) {
        optionPackageRepository.deleteBatchByParentOptionId(parentOptionId);
    }
}
