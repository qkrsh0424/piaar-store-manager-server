package com.piaar_store_manager.server.service.option_package;

import java.util.List;

import com.piaar_store_manager.server.model.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.model.option_package.repository.OptionPackageRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionPackageService {
    private final OptionPackageRepository optionPackageRepository;

    public void createList(List<OptionPackageEntity> entities) {
        optionPackageRepository.saveAll(entities);
    }
}
