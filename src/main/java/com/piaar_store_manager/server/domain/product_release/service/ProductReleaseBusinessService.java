package com.piaar_store_manager.server.domain.product_release.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.domain.option_package.service.OptionPackageService;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReleaseBusinessService {
    private final ProductReleaseService productReleaseService;
    private final ProductOptionService productOptionService;
    private final OptionPackageService optionPackageService;
    private final UserService userService;

    public ProductReleaseGetDto searchOne(Integer productReleaseCid) {
        ProductReleaseEntity entity = productReleaseService.searchOne(productReleaseCid);
        ProductReleaseGetDto dto = ProductReleaseGetDto.toDto(entity);
        return dto;
    }

    public List<ProductReleaseGetDto> searchList() {
        List<ProductReleaseEntity> entities = productReleaseService.searchList();
        List<ProductReleaseGetDto> dtos = entities.stream().map(entity -> ProductReleaseGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productReleaseCid ๋์ํ๋ release, release์ Many To One JOIN(m2oj) ์ฐ๊ด๊ด๊ณ์ ๋์ฌ์๋ product, otion, category, user๋ฅผ ํจ๊ป ์กฐํํ๋ค.
     *
     * @param productReleaseCid : Integer
     * @see ProductReleaseService#searchOneM2OJ
     */
    public ProductReleaseGetDto.ManyToOneJoin searchOneM2OJ(Integer productReleaseCid) {
        ProductReleaseProj releaseProj = productReleaseService.searchOneM2OJ(productReleaseCid);
        ProductReleaseGetDto.ManyToOneJoin resDto = ProductReleaseGetDto.ManyToOneJoin.toDto(releaseProj);
        return resDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ๋ชจ๋? release ์กฐํ, release์ Many To One JOIN(m2oj) ์ฐ๊ด๊ด๊ณ์ ๋์ฌ์๋ product, otion, category, user๋ฅผ ํจ๊ป ์กฐํํ๋ค.
     *
     * @see ProductReleaseService#searchListM2OJ
     */
    public List<ProductReleaseGetDto.ManyToOneJoin> searchListM2OJ() {
        List<ProductReleaseProj> releaseProjs = productReleaseService.searchListM2OJ();
        List<ProductReleaseGetDto.ManyToOneJoin> resDtos = releaseProjs.stream().map(proj -> ProductReleaseGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
        return resDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productOptionCid์ ๋์ํ๋ release๋ฅผ ์กฐํํ๋ค.
     *
     * @param productOptionCid : Integer
     * @see ProductReleaseService#searchListByOptionCid
     */
    public List<ProductReleaseGetDto> searchListByOptionCid(Integer productOptionCid) {
        List<ProductReleaseEntity> entities = productReleaseService.searchListByOptionCid(productOptionCid);
        List<ProductReleaseGetDto> dtos = entities.stream().map(entity -> ProductReleaseGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ๋จ์ผ release๋ฑ๋ก.
     * release๋ก ๋์ด์จ productOptionCid๋ก option ๋ฐ์ดํฐ๋ฅผ ์กฐํํ๋ค.
     * 1) - option์ packageYn์ด n์ธ ์ํ์ release ๋ฐ์ดํฐ๋ฅผ ๋ฐ๋ก ์์ฑํ๊ณ?,
     * 2) - option์ packageYn์ด y์ธ ์ํ์ package๋ฅผ ๊ตฌ์ฑํ๋ option์ ์ฐพ์ release ๋ฐ์ดํฐ ์์ฑ.
     *
     * @param productReleaseGetDto : ProductReleaseGetDto
     * @see ProductReleaseService#saveAndModify
     * @see ProductReleaseService#saveListAndModify
     * @see OptionPackageService#searchListByParentOptionId
     */
    @Transactional
    public void createOne(ProductReleaseGetDto productReleaseGetDto) {
        UUID USER_ID = userService.getUserId();
        productReleaseGetDto.setCreatedAt(CustomDateUtils.getCurrentDateTime()).setCreatedBy(USER_ID);

        ProductOptionEntity optionEntity = productOptionService.searchOne(productReleaseGetDto.getProductOptionCid());
        
        if(optionEntity.getPackageYn().equals("n")) {
            // 1) ์คํ
            ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                    .id(UUID.randomUUID())
                    .releaseUnit(productReleaseGetDto.getReleaseUnit())
                    .memo(productReleaseGetDto.getMemo())
                    .createdAt(CustomDateUtils.getCurrentDateTime())
                    .createdBy(USER_ID)
                    .productOptionCid(productReleaseGetDto.getCid())
                    .build();
            productReleaseService.saveAndModify(ProductReleaseEntity.toEntity(releaseGetDto));
        } else {
            // 2) ์คํ
            List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionId(optionEntity.getId());
            
            List<ProductReleaseEntity> productReleaseEntities = new ArrayList<>();
            optionPackageEntities.forEach(option -> {
                ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                        .id(UUID.randomUUID())
                        .releaseUnit(option.getPackageUnit() * productReleaseGetDto.getReleaseUnit())
                        .memo(productReleaseGetDto.getMemo())
                        .createdAt(CustomDateUtils.getCurrentDateTime())
                        .createdBy(USER_ID)
                        .productOptionCid(option.getOriginOptionCid())
                        .build();

                productReleaseEntities.add(ProductReleaseEntity.toEntity(releaseGetDto));
            });
            productReleaseService.saveListAndModify(productReleaseEntities);
        }
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ๋ค์ค release๋ฑ๋ก.
     * release๋ก ๋์ด์จ productOptionCid๋ก option ๋ฐ์ดํฐ๋ฅผ ์กฐํํ๋ค.
     * 1) - option์ packageYn์ด n์ธ ์ํ์ release ๋ฐ์ดํฐ๋ฅผ ๋ฐ๋ก ์์ฑ,
     * 2) - option์ packageYn์ด y์ธ ์ํ์ package๋ฅผ ๊ตฌ์ฑํ๋ option์ ์ฐพ์ release ๋ฐ์ดํฐ ์์ฑ.
     *
     * @param productReleaseGetDto : List::ProductReleaseGetDto::
     * @see ProductReleaseService#saveAndModify
     */
    @Transactional
    public void createList(List<ProductReleaseGetDto> productReleaseGetDtos) {
        UUID USER_ID = userService.getUserId();
        List<Integer> optionCids = productReleaseGetDtos.stream().map(r -> r.getProductOptionCid()).collect(Collectors.toList());
        List<ProductOptionEntity> optionEntities = productOptionService.searchListByCids(optionCids);
        List<ProductOptionEntity> originOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("n")).collect(Collectors.toList());
        List<ProductOptionEntity> parentOptionEntities = optionEntities.stream().filter(r -> r.getPackageYn().equals("y")).collect(Collectors.toList());

        // 1) ์คํ
        List<ProductReleaseEntity> productReleaseEntities = new ArrayList<>();
        productReleaseGetDtos.forEach(dto -> {
            originOptionEntities.forEach(option -> {
                if(dto.getProductOptionCid().equals(option.getCid())){
                    ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                        .id(UUID.randomUUID())
                        .releaseUnit(dto.getReleaseUnit())
                        .memo(dto.getMemo())
                        .createdAt(CustomDateUtils.getCurrentDateTime())
                        .createdBy(USER_ID)
                        .productOptionCid(option.getCid())
                        .build();

                    productReleaseEntities.add(ProductReleaseEntity.toEntity(releaseGetDto));
                }
            });
        });

        productReleaseService.saveListAndModify(productReleaseEntities);
        productReleaseEntities.clear();

        // 2) ์คํ
        if (parentOptionEntities.size() > 0) {
            List<UUID> parentOptionIdList = parentOptionEntities.stream().map(r -> r.getId()).collect(Collectors.toList());
            List<OptionPackageEntity> optionPackageEntities = optionPackageService.searchListByParentOptionIdList(parentOptionIdList);

            productReleaseGetDtos.forEach(dto -> {
                parentOptionEntities.forEach(parentOption -> {
                    if (dto.getProductOptionCid().equals(parentOption.getCid())) {
                        optionPackageEntities.forEach(option -> {
                            if (option.getParentOptionId().equals(parentOption.getId())) {
                                ProductReleaseGetDto releaseGetDto = ProductReleaseGetDto.builder()
                                        .id(UUID.randomUUID())
                                        .releaseUnit(option.getPackageUnit() * dto.getReleaseUnit())
                                        .memo(dto.getMemo())
                                        .createdAt(CustomDateUtils.getCurrentDateTime())
                                        .createdBy(USER_ID)
                                        .productOptionCid(option.getOriginOptionCid())
                                        .build();

                                productReleaseEntities.add(ProductReleaseEntity.toEntity(releaseGetDto));
                            }
                        });
                    }
                });
            });
            productReleaseService.saveListAndModify(productReleaseEntities);
        }
    }

    public void destroyOne(Integer productReleaseCid) {
        productReleaseService.destroyOne(productReleaseCid);
    }

    @Transactional
    public void changeOne(ProductReleaseGetDto releaseDto) {
        ProductReleaseEntity entity = productReleaseService.searchOne(releaseDto.getCid());
        entity.setReleaseUnit(releaseDto.getReleaseUnit()).setMemo(releaseDto.getMemo());
        productReleaseService.saveAndModify(entity);
    }

    @Transactional
    public void changeList(List<ProductReleaseGetDto> releaseDtos) {
        releaseDtos.stream().forEach(r -> this.changeOne(r));
    }

    public void patchOne(ProductReleaseGetDto releaseDto) {
        ProductReleaseEntity releaseEntity = productReleaseService.searchOne(releaseDto.getCid());

        if (releaseDto.getReleaseUnit() != null) {
            releaseEntity.setReleaseUnit(releaseDto.getReleaseUnit()).setMemo(releaseDto.getMemo());
        }
        if (releaseDto.getMemo() != null) {
            releaseEntity.setMemo(releaseDto.getMemo());
        }
        productReleaseService.saveAndModify(releaseEntity);
    }
}
