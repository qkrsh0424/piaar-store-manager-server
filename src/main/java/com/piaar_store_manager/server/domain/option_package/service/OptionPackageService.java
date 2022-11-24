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

    public List<OptionPackageProjection.RelatedProductAndOption> searchBatchByParentOptionId(UUID parentOptionId) {
        return optionPackageRepository.qfindBatchByParentOptionId(parentOptionId);
    }

    /*
     * option package, option package와 관련된 
     */
    public List<OptionPackageProjection.RelatedProductAndOption> searchBatchByParentOptionIds(List<UUID> parentOptionIds) {
        return optionPackageRepository.qfindBatchByParentOptionIds(parentOptionIds);
    }

    /*
     * parentOptionId에 대응하는 option package를 모두 제거한다.(단일 옵션에 대한 패키지 리스트 전체 제거)
     */
    public void deleteBatchByParentOptionId(UUID parentOptionId) {
        optionPackageRepository.deleteBatchByParentOptionId(parentOptionId);
    }
}
