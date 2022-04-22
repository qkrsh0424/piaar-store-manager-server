package com.piaar_store_manager.server.service.product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.model.product.dto.ProductCreateReqDto;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import com.piaar_store_manager.server.model.product.proj.ProductProj;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.service.option_package.OptionPackageService;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;

import com.piaar_store_manager.server.service.user.UserService;
import com.piaar_store_manager.server.utils.CustomUniqueKeyUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductBusinessService {
    private final ProductService productService;
    private final ProductOptionService productOptionService;
    private final OptionPackageService optionPackageService;
    private final UserService userService;

    public ProductGetDto searchOne(Integer productCid) {
        ProductEntity entity = productService.searchOne(productCid);
        return ProductGetDto.toDto(entity);
    }

    public List<ProductGetDto> searchList() {
        List<ProductEntity> entities = productService.searchList();
        List<ProductGetDto> dtos = entities.stream().map(entity -> ProductGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productCid에 대응하는 product, product와 Many To One JOIN(m2oj) 연관관계에 놓여있는 user, category를 함께 조회한다.
     *
     * @param productCid : Integer
     * @see ProductService#searchOneM2OJ
     */
    public ProductGetDto.ManyToOneJoin searchOneM2OJ(Integer productCid) {
        ProductProj productProj = productService.searchOneM2OJ(productCid);
        ProductGetDto.ManyToOneJoin productM2OJDto = ProductGetDto.ManyToOneJoin.toDto(productProj);
        return productM2OJDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productCid에 대응하는 product, product와 Full JOIN(fj) 연관관계에 놓여있는 user, category, option을 함께 조회한다.
     *
     * @param productCid : Integer
     * @see ProductService#searchOneM2OJ
     * @see ProductOptionService#searchListByProduct
     */
    public ProductGetDto.FullJoin searchOneFJ(Integer productCid) {
        ProductProj productProj = productService.searchOneM2OJ(productCid);
        List<ProductOptionEntity> optionEntities = productOptionService.searchListByProduct(productCid);
        List<ProductOptionGetDto> optionDtos = optionEntities.stream().map(r -> ProductOptionGetDto.toDto(r)).collect(Collectors.toList());

        ProductGetDto.FullJoin productFJDto = ProductGetDto.FullJoin.toDto(productProj);
        productFJDto.setOptions(optionDtos);
        return productFJDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * categortyCid에 대응하는 product를 조회한다.
     *
     * @see ProductService#searchListByCategory
     */
    public List<ProductGetDto> searchListByCategory(Integer categoryCid) {
        List<ProductEntity> entities = productService.searchListByCategory(categoryCid);
        List<ProductGetDto> productDtos = entities.stream().map(entity -> ProductGetDto.toDto(entity)).collect(Collectors.toList());
        return productDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 product 조회, product와 Many To One JOIN(m2oj) 연관관계에 놓여있는 user, category를 함께 조회한다.
     *
     * @see ProductService#searchListM2OJ
     */
    public List<ProductGetDto.ManyToOneJoin> searchListM2OJ() {
        List<ProductProj> productProjs = productService.searchListM2OJ();
        List<ProductGetDto.ManyToOneJoin> productM2OJDto = productProjs.stream().map(proj -> ProductGetDto.ManyToOneJoin.toDto(proj)).collect(Collectors.toList());
        return productM2OJDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 product 조회, product와 Full JOIN(fj) 연관관계에 놓여있는 user, category, option을 함께 조회한다.
     *
     * @see ProductService#searchListM2OJ
     * @see ProductOptionService#searchListByProductList
     */
    public List<ProductGetDto.FullJoin> searchListFJ() {
        List<ProductProj> productProjs = productService.searchListM2OJ();
        List<Integer> productCids = productProjs.stream().map(r -> r.getProduct().getCid()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByProductList(productCids);

        // option setting
        List<ProductGetDto.FullJoin> productFJDtos = productProjs.stream().map(r -> {
            List<ProductOptionGetDto> optionDtos = optionGetDtos.stream().filter(option -> r.getProduct().getCid().equals(option.getProductCid())).collect(Collectors.toList());

            ProductGetDto.FullJoin productFJDto = ProductGetDto.FullJoin.toDto(r);
            productFJDto.setOptions(optionDtos);
            return productFJDto;
        }).collect(Collectors.toList());
        return productFJDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 재고관리 여부가 true인 product 조회, product와 Full JOIN(fj) 연관관계에 놓여있는 user, category, option을 함께 조회한다.
     *
     * @see ProductService#searchListM2OJ
     * @see ProductOptionService#searchListByProductList
     */
    public List<ProductGetDto.FullJoin> searchStockListFJ() {
        List<ProductProj> productProjs = productService.searchListM2OJ();
        // 재고관리 상품 추출
        List<ProductProj> stockManagementProductProjs = productProjs.stream().filter(proj -> proj.getProduct().getStockManagement()).collect(Collectors.toList());
        List<Integer> productCids = stockManagementProductProjs.stream().map(proj -> proj.getProduct().getCid()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByProductList(productCids);

        // option setting
        List<ProductGetDto.FullJoin> productFJDtos = stockManagementProductProjs.stream().map(r -> {
            List<ProductOptionGetDto> optionDtos = optionGetDtos.stream().filter(option -> r.getProduct().getCid().equals(option.getProductCid())).collect(Collectors.toList());

            ProductGetDto.FullJoin productFJDto = ProductGetDto.FullJoin.toDto(r);
            productFJDto.setOptions(optionDtos);
            return productFJDto;
        }).collect(Collectors.toList());
        return productFJDtos;
    }

    public void createOne(ProductGetDto productGetDto) {
        UUID USER_ID = userService.getUserId();

        productGetDto.setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(USER_ID)
                .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID);

        ProductEntity entity = ProductEntity.toEntity(productGetDto);
        productService.saveAndModify(entity);
    }

    public void createList(List<ProductGetDto> productGetDto) {
        UUID USER_ID = userService.getUserId();

        List<ProductEntity> productEntities = productGetDto.stream().map(r -> {
            r.setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(USER_ID)
                    .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID);

            return ProductEntity.toEntity(r);
        }).collect(Collectors.toList());

        productService.saveListAndModify(productEntities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 단일 product 등록, 해당 product에 포함된 option을 함께 등록하고
     * 해당 option에 포함된 option package를 함께 등록한다.
     *
     * @see ProductService#saveAndGet
     * @see ProductOptionService#createList
     * @see OptionPackageService#saveListAndModify
     */
    @Transactional
    public void createPAO(ProductCreateReqDto reqDto) {
        UUID USER_ID = userService.getUserId();

        // Save Product
        reqDto.getProductDto().setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(USER_ID)
                .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID);

        ProductEntity savedEntity = productService.saveAndGet(ProductEntity.toEntity(reqDto.getProductDto()));
        ProductGetDto savedProductDto = ProductGetDto.toDto(savedEntity);

        List<ProductOptionEntity> entities = reqDto.getOptionDtos().stream().map(r -> {
            r.setCode(CustomUniqueKeyUtils.generateKey()).setCreatedAt(DateHandler.getCurrentDate2()).setCreatedBy(USER_ID)
                    .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID).setProductCid(savedProductDto.getCid());

            // 패키지 상품 여부
            if (reqDto.getPackageDtos().size() > 0) {
                r.setPackageYn("y");
            } else {
                r.setPackageYn("n");
            }

            // 옵션에 totalPurchasePrice가 입력되지 않았다면 상품의 defaultTotalPurchasePrice로 setting
            if (r.getTotalPurchasePrice() == 0) {
                r.setTotalPurchasePrice(savedProductDto.getDefaultTotalPurchasePrice());
            }

            return ProductOptionEntity.toEntity(r);
        }).collect(Collectors.toList());

        // Save ProductOption
        productOptionService.createList(entities);

        List<OptionPackageEntity> optionPackageEntities = reqDto.getPackageDtos().stream().map(r -> {
            r.setCreatedAt(LocalDateTime.now()).setCreatedBy(USER_ID)
                    .setUpdatedAt(LocalDateTime.now()).setUpdatedBy(USER_ID);

            return OptionPackageEntity.toEntity(r);
        }).collect(Collectors.toList());

        // Save OptionPackage
        optionPackageService.saveListAndModify(optionPackageEntities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * createPAO를 반복하여 실행한다.
     *
     * @see ProductBusinessService#createPAO
     */
    @Transactional
    public void createPAOList(List<ProductCreateReqDto> productCreateReqDtos) {
        productCreateReqDtos.stream().forEach(r -> this.createPAO(r));
    }

    public void destroyOne(Integer productCid) {
        productService.destroyOne(productCid);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 단일 product수정, product의 매입총합계에 따라 하위 option들의 매입총합계도 함께 수정한다.
     *
     * @see ProductService#saveAndModify
     * @see ProductOptionService#createList
     */
    public void changeOne(ProductGetDto productDto) {
        UUID USER_ID = userService.getUserId();

        ProductEntity productEntity = productService.searchOne(productDto.getCid());
        productEntity.setCode(productDto.getCode()).setManufacturingCode(productDto.getManufacturingCode())
                .setNaverProductCode(productDto.getNaverProductCode()).setDefaultName(productDto.getDefaultName())
                .setManagementName(productDto.getManagementName()).setImageUrl(productDto.getImageUrl())
                .setImageFileName(productDto.getImageFileName()).setPurchaseUrl(productDto.getPurchaseUrl()).setMemo(productDto.getMemo())
                .setHsCode(productDto.getHsCode()).setStyle(productDto.getStyle())
                .setTariffRate(productDto.getTariffRate()).setDefaultWidth(productDto.getDefaultWidth())
                .setDefaultLength(productDto.getDefaultLength()).setDefaultHeight(productDto.getDefaultHeight())
                .setDefaultQuantity(productDto.getDefaultQuantity()).setDefaultWeight(productDto.getDefaultWeight())
                .setDefaultTotalPurchasePrice(productDto.getDefaultTotalPurchasePrice())
                .setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID)
                .setStockManagement(productDto.getStockManagement())
                .setProductCategoryCid(productDto.getProductCategoryCid());

        productService.saveAndModify(productEntity);

        // 옵션들의 매입총합계를 변경한다.
        List<ProductOptionEntity> optionEntities = productOptionService.searchListByProduct(productEntity.getCid());
        optionEntities.stream().forEach(r -> r.setTotalPurchasePrice(productEntity.getDefaultTotalPurchasePrice()));

        productOptionService.createList(optionEntities);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * changeOne를 반복하여 실행한다.
     *
     * @see ProductBusinessService#changeOne
     */
    @Transactional
    public void changePAOList(List<ProductCreateReqDto> productCreateReqDtos) {
        productCreateReqDtos.stream().forEach(req -> {
            this.changeOne(req.getProductDto());
        });
    }

    public void patchOne(ProductGetDto productDto) {
        UUID USER_ID = userService.getUserId();

        ProductEntity productEntity = productService.searchOne(productDto.getCid());

        if (productDto.getCode() != null) {
            productEntity.setCode(productDto.getCode());
        }
        if (productDto.getManufacturingCode() != null) {
            productEntity.setManufacturingCode(productDto.getManufacturingCode());
        }
        if (productDto.getNaverProductCode() != null) {
            productEntity.setNaverProductCode(productDto.getNaverProductCode());
        }
        if (productDto.getDefaultName() != null) {
            productEntity.setDefaultName(productDto.getDefaultName());
        }
        if (productDto.getManagementName() != null) {
            productEntity.setManagementName(productDto.getManagementName());
        }
        if (productDto.getImageUrl() != null) {
            productEntity.setImageUrl(productDto.getImageUrl());
        }
        if (productDto.getImageFileName() != null) {
            productEntity.setImageFileName(productDto.getImageFileName());
        }
        if (productDto.getPurchaseUrl() != null) {
            productEntity.setPurchaseUrl(productDto.getPurchaseUrl());
        }
        if (productDto.getMemo() != null) {
            productEntity.setMemo(productDto.getMemo());
        }
        if (productDto.getHsCode() != null) {
            productEntity.setHsCode(productDto.getHsCode());
        }
        if (productDto.getStyle() != null) {
            productEntity.setStyle(productDto.getStyle());
        }
        if (productDto.getTariffRate() != null) {
            productEntity.setTariffRate(productDto.getTariffRate());
        }
        if (productDto.getDefaultWidth() != null) {
            productEntity.setDefaultWidth(productDto.getDefaultWidth());
        }
        if (productDto.getDefaultLength() != null) {
            productEntity.setDefaultLength(productDto.getDefaultLength());
        }
        if (productDto.getDefaultHeight() != null) {
            productEntity.setDefaultHeight(productDto.getDefaultHeight());
        }
        if (productDto.getDefaultQuantity() != null) {
            productEntity.setDefaultQuantity(productDto.getDefaultQuantity());
        }
        if (productDto.getDefaultWeight() != null) {
            productEntity.setDefaultWeight(productDto.getDefaultWeight());
        }
        if (productDto.getDefaultTotalPurchasePrice() != null) {
            productEntity.setDefaultTotalPurchasePrice(productDto.getDefaultTotalPurchasePrice());
        }
        if (productDto.getStockManagement() != null) {
            productEntity.setStockManagement(productDto.getStockManagement());
        }
        if (productDto.getProductCategoryCid() != null) {
            productEntity.setProductCategoryCid(productDto.getProductCategoryCid());
        }
        productEntity.setUpdatedAt(DateHandler.getCurrentDate2()).setUpdatedBy(USER_ID);
        productService.saveAndModify(productEntity);
    }
}
