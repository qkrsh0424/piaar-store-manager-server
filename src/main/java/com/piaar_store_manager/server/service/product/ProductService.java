package com.piaar_store_manager.server.service.product;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product.dto.ProductCreateReqDto;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product.dto.ProductJoinResDto;
import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import com.piaar_store_manager.server.model.product.proj.ProductProj;
import com.piaar_store_manager.server.model.product.repository.ProductRepository;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;

import com.piaar_store_manager.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DateHandler dateHandler;

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private UserService userService;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 조회한다.
     *
     * @param productCid : Integer
     * @return ProductGetDto
     * @see ProductRepository#findById
     */
    public ProductGetDto searchOne(Integer productCid) {
        Optional<ProductEntity> productEntityOpt = productRepository.findById(productCid);
        ProductGetDto productDto = new ProductGetDto();

        if (productEntityOpt.isPresent()) {
            productDto = ProductGetDto.toDto(productEntityOpt.get());
        } else {
            throw new NullPointerException();
        }

        return productDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 조회한다.
     * 해당 Product와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @param productCid : Integer
     * @return ProductJoinResDto
     * @see ProductRepository#selectByCid
     * @see ProductGetDto#toDto
     * @see UserService#getDtoByEntity
     * @see ProductCategoryGetDto#toDto
     */
    public ProductJoinResDto searchOneM2OJ(Integer productCid) {
        ProductJoinResDto productResDto = new ProductJoinResDto();
        
        Optional<ProductProj> productProjOpt = productRepository.selectByCid(productCid);

        if(productProjOpt.isPresent()){
            ProductGetDto productGetDto = ProductGetDto.toDto(productProjOpt.get().getProduct());
            UserGetDto userGetDto = userService.getDtoByEntity(productProjOpt.get().getUser());
            ProductCategoryGetDto categoryGetDto = ProductCategoryGetDto.toDto(productProjOpt.get().getCategory());

            productResDto
                .setProduct(productGetDto)
                .setUser(userGetDto)
                .setCategory(categoryGetDto);

        }else{
            throw new NullPointerException();
        }
        return productResDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 조회한다.
     * 해당 Product와 연관관계에 놓여있는 Full JOIN(fj) 상태를 조회한다.
     *
     * @param productCid : Integer
     * @return ProductJoinResDto
     * @see ProductRepository#selectByCid
     * @see UserService#getDtoByEntity
     * @see ProductCategoryGetDto#toDto
     * @see ProductOptionService#searchList
     */
    public ProductJoinResDto searchOneFJ(Integer productCid) {
        ProductJoinResDto productResDto = new ProductJoinResDto();
        
        Optional<ProductProj> productProjOpt = productRepository.selectByCid(productCid);

        if(productProjOpt.isPresent()){
            ProductGetDto productGetDto = ProductGetDto.toDto(productProjOpt.get().getProduct());
            UserGetDto userGetDto = userService.getDtoByEntity(productProjOpt.get().getUser());
            ProductCategoryGetDto categoryGetDto = ProductCategoryGetDto.toDto(productProjOpt.get().getCategory());
            List<ProductOptionGetDto> optionGetDtos = productOptionService.searchList(productProjOpt.get().getProduct().getCid());
            
            productResDto
                .setProduct(productGetDto)
                .setUser(userGetDto)
                .setCategory(categoryGetDto)
                .setOptions(optionGetDtos);

        }else{
            throw new NullPointerException();
        }
        return productResDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product 데이터를 모두 조회한다.
     * 
     * @return List::ProductGetDto::
     * @see ProductRepository#findAll
     * @see ProductGetDto#toDto
     */
    public List<ProductGetDto> searchList() {
        List<ProductEntity> productEntities = productRepository.findAll();
        List<ProductGetDto> productDto = new ArrayList<>();

        for(ProductEntity entity : productEntities){
            productDto.add(ProductGetDto.toDto(entity));
        }
        return productDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product 데이터를 모두 조회한다.
     * 해당 Product와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return List::ProductJoinResDto::
     * @see ProductRepository#selectAll
     */
    public List<ProductJoinResDto> searchListM2OJ() {
        List<ProductJoinResDto> productJoinResDtos = new ArrayList<>();
        List<ProductProj> productProjsOpt = productRepository.selectAll();
        
        for(ProductProj projOpt : productProjsOpt) {
            productJoinResDtos.add(searchOneM2OJ(projOpt.getProduct().getCid()));
        }
        return productJoinResDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product 데이터를 모두 조회한다.
     * 해당 Product와 연관관계에 놓여있는 모든 Full JOIN(fj) 상태를 조회한다.
     *
     * @return List::ProductJoinResDto::
     * @see ProductRepository#selectAll
     */
    public List<ProductJoinResDto> searchListFJ(){
        List<ProductJoinResDto> productJoinResDtos = new ArrayList<>();
        List<ProductProj> productProjsOpt = productRepository.selectAll();
        
        for(ProductProj projOpt : productProjsOpt) {

            // TODO
            // log.info("projOpt => {}", projOpt.getProduct().getDefaultName());

            // 재고관리 여부
            if(projOpt.getProduct().getStockManagement())
                productJoinResDtos.add(searchOneFJ(projOpt.getProduct().getCid()));
        }
        return productJoinResDtos;

    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product 내용을 한개 등록한다.
     * 
     * @param productGetDto : ProductGetDto
     * @param userId : UUID
     * @see ProductEntity#toEntity
     * @see ProductRepository#save
     */
    // public void createOne(ProductGetDto productGetDto, UUID userId) {
    //     productGetDto.setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId)
    //         .setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId);

    //     ProductEntity entity = ProductEntity.toEntity(productGetDto);
    //     productRepository.save(entity);
    // }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product와 ProductOption 내용을 한개 등록한다.
     * 
     * @param productCreateReqDto : ProductCreateReqDto
     * @param userId : UUID
     * @see ProductEntity#toEntity
     * @see ProductRepository#save
     * @see ProductOptionService#createList
     */
    // public void createPAO(ProductCreateReqDto productCreateReqDto, UUID userId) {
    //     ProductGetDto dto = productCreateReqDto.getProductDto();
    //     dto.setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId)
    //         .setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId);

    //     ProductEntity entity = ProductEntity.toEntity(productCreateReqDto.getProductDto());
    //     ProductEntity savedProductEntity = productRepository.save(entity);

    //     productOptionService.createList(productCreateReqDto, userId, savedProductEntity.getCid());
    // }

    public ProductEntity createOne(ProductGetDto dto, UUID userId) {
        
        dto.setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId)
            .setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId);

        ProductEntity entity = ProductEntity.toEntity(dto);

        return productRepository.save(entity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product와 ProductOption 내용을 여러개 등록한다.
     * 
     * @param productCreateReqDtos : List::ProductCreateReqDto::
     * @param userId : UUID
     * @see ProductEntity#toEntity
     * @see ProductRepository#save
     * @see ProductOptionService#createList
     */
    // public void createPAOList(List<ProductCreateReqDto> productCreateReqDtos, UUID userId) {
    //     ProductEntity entity = new ProductEntity();
    //     ProductEntity savedProductEntity = new ProductEntity();

    //     for (ProductCreateReqDto reqDto : productCreateReqDtos) {
    //         ProductGetDto dto = reqDto.getProductDto();
    //         dto.setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId)
    //             .setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId);

    //         entity = ProductEntity.toEntity(reqDto.getProductDto());
    //         savedProductEntity = productRepository.save(entity);
    //         productOptionService.createList(reqDto, userId, savedProductEntity.getCid());
    //     }
    // }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productCid : Integer
     * @see ProductRepository#findById
     * @see ProductRepository#delete
     */
    public void destroyOne(Integer productCid) {
        productRepository.findById(productCid).ifPresent(product -> {
            productRepository.delete(product);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param productDto : ProductGetDto
     * @param userId : UUID
     * @see ProductRepository#findById
     * @see ProductRepository#save
     */
    public void changeOne(ProductGetDto productDto, UUID userId) {
        productRepository.findById(productDto.getCid()).ifPresentOrElse(productEntity -> {
            productEntity.setCode(productDto.getCode()).setManufacturingCode(productDto.getManufacturingCode())
                    .setNaverProductCode(productDto.getNaverProductCode()).setDefaultName(productDto.getDefaultName())
                    .setManagementName(productDto.getManagementName()).setImageUrl(productDto.getImageUrl())
                    .setImageFileName(productDto.getImageFileName()).setMemo(productDto.getMemo())
                    .setHsCode(productDto.getHsCode()).setStyle(productDto.getStyle())
                    .setTariffRate(productDto.getTariffRate()).setDefaultWidth(productDto.getDefaultWidth())
                    .setDefaultLength(productDto.getDefaultLength()).setDefaultHeight(productDto.getDefaultHeight())
                    .setDefaultQuantity(productDto.getDefaultQuantity()).setDefaultWeight(productDto.getDefaultWeight())
                    .setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId).setStockManagement(productDto.getStockManagement())
                    .setProductCategoryCid(productDto.getProductCategoryCid());

            productRepository.save(productEntity);
        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 각 상품마다 ProductOption cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param productCreateReqDtos : List::ProductCreateReqDto::
     * @param userId :: UUID
     * @see ProductOptionService#changeOne
     */
    public void changePAOList(List<ProductCreateReqDto> productCreateReqDtos, UUID userId) {

        for (ProductCreateReqDto dto : productCreateReqDtos) {
            changeOne(dto.getProductDto(), userId);

            for(ProductOptionGetDto optionDto : dto.getOptionDtos()) {
                productOptionService.changeOne(optionDto, userId);
            }
        }
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * Product id 값과 상응되는 데이터의 일부분을 업데이트한다.
     * 
     * @param productDto : ProductGetDto
     * @param userId : UUID
     * @see ProductRepository#findById
     * @see ProductRepository#save
     */
    public void patchOne(ProductGetDto productDto, UUID userId) {
        productRepository.findById(productDto.getCid()).ifPresentOrElse(productEntity -> {
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
            if (productDto.getStockManagement() != null) {
                productEntity.setStockManagement(productDto.getStockManagement());
            }
            if (productDto.getProductCategoryCid() != null) {
                productEntity.setProductCategoryCid(productDto.getProductCategoryCid());
            }

            productEntity.setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId);

            productRepository.save(productEntity);
        }, null);
    }
}
