package com.piaar_store_manager.server.service.option_package;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.model.option_package.repository.OptionPackageRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptionPackageService {
    private final OptionPackageRepository optionPackageRepository;

    public void saveListAndModify(List<OptionPackageEntity> entities) {
        optionPackageRepository.saveAll(entities);
    }

    public List<OptionPackageEntity> searchListByParentOptionId(UUID parentOptionId) {
        return optionPackageRepository.findAllByParentOptionId(parentOptionId);
    }

    public List<OptionPackageEntity> searchListByParentOptionIdList(List<UUID> parentOptionIdList) {
        return optionPackageRepository.findAllByParentOptionIdList(parentOptionIdList);
    }

    public void deleteBatch(List<UUID> idList) {
        optionPackageRepository.deleteBatch(idList);
    }
}
