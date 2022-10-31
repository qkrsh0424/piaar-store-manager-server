package com.piaar_store_manager.server.domain.option_package.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.option_package.dto.OptionPackageDto;
import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.user.service.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionPackageBusinessServiceV2 {
    private final OptionPackageService optionPackageService;
    private final ProductOptionService productOptionService;
    private final UserService userService;

    /**
     * <b>DB Select Related Method</b>
     * parentOptionId에 대응하는 option package를 모두 조회한다.
     * 
     * @param parentOptionId : UUID
     * @return List::OptionPackageDto::
     * @see OptionPackageService#searchBatchByParentOptionId
     */
    public List<OptionPackageDto> searchBatchByParentOptionId(UUID parentOptionId) {
        List<OptionPackageEntity> entities = optionPackageService.searchBatchByParentOptionId(parentOptionId);
        List<OptionPackageDto> dtos = entities.stream().map(entity -> OptionPackageDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    @Transactional
    public void deleteAndCreateBatch(UUID parentOptionId, List<OptionPackageDto> dtos) {
        UUID USER_ID = userService.getUserId();
        ProductOptionEntity optionEntity = productOptionService.searchOne(parentOptionId);

        // 옵션패키지 제거
        optionPackageService.deleteBatchByParentOptionId(optionEntity.getId());

        // null 이거나 dtos 사이즈가 0일 때
        // 옵션의 packageYn 수정, 옵션패키지 제거
        if(dtos == null || dtos.isEmpty()) {
            optionEntity.setPackageYn("n").setUpdatedAt(LocalDateTime.now()).setUpdatedBy(USER_ID);
        } else {
            // 옵션의 packageYn 수정, 옵션패키지 제거 후 생성
            optionEntity.setPackageYn("y").setUpdatedAt(LocalDateTime.now()).setUpdatedBy(USER_ID);

            List<OptionPackageEntity> newOptionPackageEntities = dtos.stream().map(r -> {
                r.setParentOptionId(optionEntity.getId())
                    .setCreatedAt(LocalDateTime.now())
                    .setCreatedBy(USER_ID)
                    .setUpdatedAt(LocalDateTime.now())
                    .setUpdatedBy(USER_ID);
                return OptionPackageEntity.toEntity(r);
            }).collect(Collectors.toList());

            optionPackageService.saveListAndModify(newOptionPackageEntities);
        }
    }
}
