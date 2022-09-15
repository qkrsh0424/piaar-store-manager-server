package com.piaar_store_manager.server.domain.option_package.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.option_package.dto.OptionPackageDto;
import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionPackageBusinessService {
    private final OptionPackageService optionPackageService;

    /**
     * <b>DB Select Related Method</b>
     * parentOptionId에 대응하는 option package를 모두 조회한다.
     * 
     * @param parentOptionId : UUID
     * @return List::OptionPackageDto::
     * @see OptionPackageService#searchListByParentOptionId
     */
    public List<OptionPackageDto> searchListByParentOptionId(UUID parentOptionId) {
        List<OptionPackageEntity> entities = optionPackageService.searchListByParentOptionId(parentOptionId);
        List<OptionPackageDto> dtos = entities.stream().map(entity -> OptionPackageDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    public void deleteBatchByParentOptionId(UUID parentOptionId) {
        optionPackageService.deleteBatchByParentOptionId(parentOptionId);
    }
}
