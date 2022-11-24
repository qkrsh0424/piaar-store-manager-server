package com.piaar_store_manager.server.domain.option_package.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.option_package.dto.OptionPackageDto;
import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.domain.option_package.proj.OptionPackageProjection;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionPackageBusinessServiceV2 {
    private final OptionPackageService optionPackageService;
    private final ProductOptionService productOptionService;
    private final UserService userService;

    public List<OptionPackageDto.RelatedOriginOption> searchBatchByParentOptionId(UUID parentOptionId) {
        List<OptionPackageProjection.RelatedProductAndOption> projs = optionPackageService.searchBatchByParentOptionId(parentOptionId);
        List<OptionPackageDto.RelatedOriginOption> dtos = projs.stream().map(proj -> OptionPackageDto.RelatedOriginOption.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }

    /*
     * 옵션에 등록된 패키지옵션들을 모두 제거한 후, 새로 등록한다
     */
    @Transactional
    public void deleteAndCreateBatch(UUID parentOptionId, List<OptionPackageDto> dtos) {
        UUID USER_ID = userService.getUserId();
        ProductOptionEntity optionEntity = productOptionService.searchOne(parentOptionId);

        // 옵션패키지 제거
        optionPackageService.deleteBatchByParentOptionId(optionEntity.getId());

        // 옵션패키지가 존재하지 않을 경우, option의 packageYn을 n으로 변경
        if(dtos == null || dtos.isEmpty()) {
            optionEntity.setPackageYn("n").setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);
        } else {
            // 옵션의 packageYn 수정, 옵션패키지 제거 후 생성
            optionEntity.setPackageYn("y").setUpdatedAt(CustomDateUtils.getCurrentDateTime()).setUpdatedBy(USER_ID);

            List<OptionPackageEntity> newOptionPackageEntities = dtos.stream().map(r -> {
                OptionPackageEntity entity = OptionPackageEntity.builder()
                    .id(UUID.randomUUID())
                    .packageUnit(r.getPackageUnit())
                    .originOptionId(r.getOriginOptionId())
                    .createdAt(CustomDateUtils.getCurrentDateTime())
                    .createdBy(USER_ID)
                    .updatedAt(CustomDateUtils.getCurrentDateTime())
                    .updatedBy(USER_ID)
                    .parentOptionId(optionEntity.getId())
                    .build();

                return entity;
            }).collect(Collectors.toList());

            optionPackageService.saveListAndModify(newOptionPackageEntities);
        }
    }
}
