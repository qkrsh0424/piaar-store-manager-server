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
import com.piaar_store_manager.server.service.product_category.ProductCategoryService;
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
    private ProductCategoryService productCategoryService; 

    @Autowired
    private UserService userService;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductEntity => ProductGetDto
     * 
     * @param productEntity : ProductEntity
     * @return ProductGetDto
     */
    public ProductGetDto getDtoByEntity(ProductEntity productEntity) {
        ProductGetDto productDto = new ProductGetDto();

        productDto.setCid(productEntity.getCid()).setId(productEntity.getId()).setCode(productEntity.getCode())
                .setManufacturingCode(productEntity.getManufacturingCode())
                .setNaverProductCode(productEntity.getNaverProductCode()).setDefaultName(productEntity.getDefaultName())
                .setManagementName(productEntity.getManagementName()).setImageUrl(productEntity.getImageUrl())
                .setImageFileName(productEntity.getImageFileName()).setMemo(productEntity.getMemo())
                .setCreatedAt(productEntity.getCreatedAt()).setCreatedBy(productEntity.getCreatedBy())
                .setUpdatedAt(productEntity.getUpdatedAt()).setUpdatedBy(productEntity.getUpdatedBy())
                .setProductCategoryCid(productEntity.getProductCategoryCid());

        return productDto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductGetDto => ProductEntity
     * 
     * @param productDto : ProductGetDto
     * @param userId : UUID
     * @return ProductEntity
     */
    private ProductEntity convEntityByDto(ProductGetDto productDto, UUID userId) {
        ProductEntity productEntity = new ProductEntity();

        productEntity.setId(UUID.randomUUID()).setCode(productDto.getCode())
                .setManufacturingCode(productDto.getManufacturingCode()).setNaverProductCode(productDto.getNaverProductCode())
                .setDefaultName(productDto.getDefaultName()).setManagementName(productDto.getManagementName())
                .setImageUrl(productDto.getImageUrl()).setImageFileName(productDto.getImageFileName())
                .setMemo(productDto.getMemo()).setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId)
                .setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId)
                .setProductCategoryCid(productDto.getProductCategoryCid());

        return productEntity;
    }

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
            productDto = getDtoByEntity(productEntityOpt.get());
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
     * @see UserService#getDtoByEntity
     * @see ProductCategoryService#getDtoByEntity
     */
    public ProductJoinResDto searchOneM2OJ(Integer productCid) {
        ProductJoinResDto productResDto = new ProductJoinResDto();
        
        Optional<ProductProj> productProjOpt = productRepository.selectByCid(productCid);

        if(productProjOpt.isPresent()){
            ProductGetDto productGetDto = this.getDtoByEntity(productProjOpt.get().getProduct());
            UserGetDto userGetDto = userService.getDtoByEntity(productProjOpt.get().getUser());
            ProductCategoryGetDto categoryGetDto = productCategoryService.getDtoByEntity(productProjOpt.get().getCategory());

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
     * @see ProductCategoryService#getDtoByEntity
     * @see ProductOptionService#searchList
     */
    public ProductJoinResDto searchOneFJ(Integer productCid) {
        ProductJoinResDto productResDto = new ProductJoinResDto();
        
        Optional<ProductProj> productProjOpt = productRepository.selectByCid(productCid);

        if(productProjOpt.isPresent()){
            ProductGetDto productGetDto = this.getDtoByEntity(productProjOpt.get().getProduct());
            UserGetDto userGetDto = userService.getDtoByEntity(productProjOpt.get().getUser());
            ProductCategoryGetDto categoryGetDto = productCategoryService.getDtoByEntity(productProjOpt.get().getCategory());
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
     */
    public List<ProductGetDto> searchList() {
        List<ProductEntity> productEntities = productRepository.findAll();
        List<ProductGetDto> productDto = new ArrayList<>();

        for(ProductEntity entity : productEntities){
            productDto.add(getDtoByEntity(entity));
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
     * @see ProductRepository#save
     */
    public void createOne(ProductGetDto productGetDto, UUID userId) {
        ProductEntity entity = convEntityByDto(productGetDto, userId);
        productRepository.save(entity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product와 ProductOption 내용을 한개 등록한다.
     * 
     * @param productCreateReqDto : ProductCreateReqDto
     * @param userId : UUID
     * @see ProductRepository#save
     * @see ProductOptionService#createList
     */
    public void createPAO(ProductCreateReqDto productCreateReqDto, UUID userId) {
        ProductEntity entity = convEntityByDto(productCreateReqDto.getProductDto(), userId);
        ProductEntity savedProductEntity = productRepository.save(entity);

        productOptionService.createList(productCreateReqDto.getOptionDtos(), userId, savedProductEntity.getCid());
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product와 ProductOption 내용을 여러개 등록한다.
     * 
     * @param productCreateReqDtos : List::ProductCreateReqDto::
     * @param userId : UUID
     * @see ProductRepository#save
     * @see ProductOptionService#createList
     */
    public void createPAOList(List<ProductCreateReqDto> productCreateReqDtos, UUID userId) {
        ProductEntity entity = new ProductEntity();
        ProductEntity savedProductEntity = new ProductEntity();

        for (ProductCreateReqDto dto : productCreateReqDtos) {
            entity = convEntityByDto(dto.getProductDto(), userId);
            savedProductEntity = productRepository.save(entity);
            productOptionService.createList(dto.getOptionDtos(), userId, savedProductEntity.getCid());
        }
    }

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
                    .setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId)
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
            if (productDto.getProductCategoryCid() != null) {
                productEntity.setProductCategoryCid(productDto.getProductCategoryCid());
            }

            productEntity.setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId);

            productRepository.save(productEntity);
        }, null);
    }
}
