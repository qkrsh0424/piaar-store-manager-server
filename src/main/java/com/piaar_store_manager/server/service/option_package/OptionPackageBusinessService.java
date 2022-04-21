package com.piaar_store_manager.server.service.option_package;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.model.option_package.dto.OptionPackageDto;
import com.piaar_store_manager.server.model.option_package.entity.OptionPackageEntity;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionPackageBusinessService {
    private final OptionPackageService optionPackageService;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Option id 값과 상응되는 데이터를 조회한다.
     *
     * @param parentOptionId : UUID
     * @return List::OptionPackageDto::
     * @see optionPackageService#searchOneByParentOptionId
     * @see OptionPackageDto#toDto
     */
    public List<OptionPackageDto> searchListByParentOptionId(UUID parentOptionId) {
        List<OptionPackageEntity> entities = optionPackageService.searchListByParentOptionId(parentOptionId);
        List<OptionPackageDto> dtos = entities.stream().map(entity -> OptionPackageDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }
}
