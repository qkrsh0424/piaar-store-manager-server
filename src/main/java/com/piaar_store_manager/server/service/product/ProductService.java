package com.piaar_store_manager.server.service.product;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product.dto.ProductCreateReqDto;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product.dto.ProductJoinResDto;
import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import com.piaar_store_manager.server.model.product.entity.ProductJEntity;
import com.piaar_store_manager.server.model.product.proj.ProductProj;
import com.piaar_store_manager.server.model.product.repository.ProductJRepository;
import com.piaar_store_manager.server.model.product.repository.ProductRepository;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
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
    UserService userService;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product id 값과 상응되는 데이터를 조회한다.
     *
     * @param productId
     * @return ProductGetDto
     * @see ProductRepository
     */
    public ProductGetDto searchOne(Integer productId) {
        Optional<ProductEntity> productEntityOpt = productRepository.findById(productId);
        ProductGetDto productDto = new ProductGetDto();

        if (productEntityOpt.isPresent()) {
            productDto = getDtoByEntity(productEntityOpt.get());
        } else {
            throw new NullPointerException();
        }

        return productDto;
    }

    // TODO(READ) :: ADD NEW 
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product id 값과 상응되는 데이터를 조회한다.
     * 해당 Product와 연관관계에 놓여있는 One To Many JOIN(o2mj) 상태를 조회한다.
     * 
     *
     * @param productId
     * @return ProductGetDto
     * @see ProductRepository#selectByCid
     * @see UserEntity
     * @see ProductCategoryEntity
     * 
     * @return ProductJoinResDto
     */
    public ProductJoinResDto searchOneO2MJ(Integer productId) {
        ProductJoinResDto productResDto = new ProductJoinResDto();
        
        Optional<ProductProj> productProjOpt = productRepository.selectByCid(productId);

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

    // TODO(READ) :: ADD NEW 
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product id 값과 상응되는 데이터를 조회한다.
     * 해당 Product와 연관관계에 놓여있는 모든 JOIN(fj) 상태를 조회한다.
     * 
     *
     * @param productCid
     * @return ProductGetDto
     * @see ProductRepository#selectByCid
     * @see UserEntity
     * @see ProductCategoryEntity
     * @see ProductOptionEntity
     * 
     * @return ProductJoinResDto
     */
    public ProductJoinResDto searchOneFullJoin(Integer productCid) {
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
     * <b>Convert Method</b>
     * <p>
     * ProductEntity => ProductGetDto
     * 
     * @param productEntity
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
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 상품들은 모두 조회한다.
     *
     * @return List::ProductGetDto::
     * @see ProductRepository
     */
    public List<ProductGetDto> searchList() {
        List<ProductEntity> productEntities = productRepository.findAll();
        List<ProductGetDto> productDto = new ArrayList<>();

        for(ProductEntity entity : productEntities){
            productDto.add(getDtoByEntity(entity));
        }
        return productDto;
    }

    // TODO(READ) :: ADD NEW
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product 데이터를 모두 조회한다.
     * 해당 Product와 연관관계에 놓여있는 One To Many JOIN(o2mj) 상태를 조회한다.
     *
     *
     * @return List::ProductJoinResDto::
     * @see ProductRepository#selectAll
     * @see UserEntity
     * @see ProductCategoryEntity
     *
     * @return ProductJoinResDto
     */
    public List<ProductJoinResDto> searchListO2MJ() {
        List<ProductJoinResDto> productJoinResDtos = new ArrayList<>();
        List<ProductProj> productProjsOpt = productRepository.selectAll();
        
        for(ProductProj projOpt : productProjsOpt) {
            ProductJoinResDto productJoinResDto = new ProductJoinResDto();

            ProductGetDto productGetDto = this.getDtoByEntity(projOpt.getProduct());
            UserGetDto userGetDto = userService.getDtoByEntity(projOpt.getUser());
            ProductCategoryGetDto categoryGetDto = productCategoryService.getDtoByEntity(projOpt.getCategory());

            productJoinResDto
                    .setProduct(productGetDto)
                    .setUser(userGetDto)
                    .setCategory(categoryGetDto);

            productJoinResDtos.add(productJoinResDto);
        }
        return productJoinResDtos;
    }

    // TODO(READ) :: ADD NEW
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product 데이터를 모두 조회한다.
     * 해당 Product와 연관관계에 놓여있는 모든 JOIN(fj) 상태를 조회한다.
     *
     *
     * @return List::ProductJoinResDto::
     * @see ProductRepository#selectAll
     * @see UserEntity
     * @see ProductCategoryEntity
     * @see ProductOptionEntity
     *
     * @return ProductJoinResDto
     */
    public List<ProductJoinResDto> searchListFullJoin(){
        List<ProductJoinResDto> productJoinResDtos = new ArrayList<>();
        List<ProductProj> productProjsOpt = productRepository.selectAll();
        
        for(ProductProj projOpt : productProjsOpt) {
            ProductJoinResDto productJoinResDto = new ProductJoinResDto();

            ProductGetDto productGetDto = this.getDtoByEntity(projOpt.getProduct());
            UserGetDto userGetDto = userService.getDtoByEntity(projOpt.getUser());
            ProductCategoryGetDto categoryGetDto = productCategoryService.getDtoByEntity(projOpt.getCategory());
            List<ProductOptionGetDto> optionGetDtos = productOptionService.searchList(projOpt.getProduct().getCid());

            productJoinResDto
                    .setProduct(productGetDto)
                    .setUser(userGetDto)
                    .setCategory(categoryGetDto)
                    .setOptions(optionGetDtos);

            productJoinResDtos.add(productJoinResDto);
        }
        return productJoinResDtos;

    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product 내용을 한개 등록한다.
     * 
     * @param productGetDto
     * @param userId
     * @see ProductRepository
     */
    public void createOne(ProductGetDto productGetDto, UUID userId) {
        ProductEntity entity = convEntitiyByDto(productGetDto, userId);
        productRepository.save(entity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product와 ProductOption 내용을 같이 등록한다.
     * 
     * @param productCreateReqDto
     * @param userId
     */
    public void createPAO(ProductCreateReqDto productCreateReqDto, UUID userId) {
        ProductEntity entity = convEntitiyByDto(productCreateReqDto.getProductDto(), userId);
        ProductEntity savedProductEntity = productRepository.save(entity);

        productOptionService.createList(productCreateReqDto.getOptionDtos(), userId, savedProductEntity.getCid());
    }

    private ProductEntity convEntityByDto(ProductGetDto productGetDto, UUID userId) {
        ProductEntity productEntity = new ProductEntity();

        productEntity.setId(productGetDto.getId()).setCode(productGetDto.getCode())
                .setManufacturingCode(productGetDto.getManufacturingCode())
                .setNaverProductCode(productGetDto.getNaverProductCode()).setDefaultName(productGetDto.getDefaultName())
                .setManagementName(productGetDto.getManagementName()).setImageUrl(productGetDto.getImageUrl())
                .setImageFileName(productGetDto.getImageFileName()).setMemo(productGetDto.getMemo())
                .setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId)
                .setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId)
                .setProductCategoryCid(productGetDto.getProductCategoryCid());

        return productEntity;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductGetDto => ProductEntity
     * 
     * @param productDto
     * @param userId
     * @return ProductEntity
     */
    private ProductEntity convEntitiyByDto(ProductGetDto productDto, UUID userId) {
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
     * <b>DB Insert Related Method</b>
     * <p>
     * Product 내용을 여러개 등록한다.
     * 
     * @param productCreateReqDtos
     * @param userId
     * @see ProductRepository
     */

    public void createPAOList(List<ProductCreateReqDto> productCreateReqDtos, UUID userId) {
        ProductEntity entity = new ProductEntity();
        ProductEntity savedProductEntity = new ProductEntity();

        for (ProductCreateReqDto dto : productCreateReqDtos) {
            entity = convEntitiyByDto(dto.getProductDto(), userId);
            savedProductEntity = productRepository.save(entity);
            productOptionService.createList(dto.getOptionDtos(), userId, savedProductEntity.getCid());
        }
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * List::ProductGetDto:: => List::ProductEntity::
     * 
     * @param productGetDtos
     * @param userId
     * @return List::ProductEntity::
     */
    private List<ProductEntity> convEntitiesByDtos(List<ProductGetDto> productGetDtos, UUID userId) {
        List<ProductEntity> productEntities = new ArrayList<>();

        for (ProductGetDto dto : productGetDtos) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(UUID.randomUUID()).setCode(dto.getCode())
                    .setManufacturingCode(dto.getManufacturingCode()).setNaverProductCode(dto.getNaverProductCode())
                    .setDefaultName(dto.getDefaultName()).setManagementName(dto.getManagementName())
                    .setImageUrl(dto.getImageUrl()).setImageFileName(dto.getImageFileName()).setMemo(dto.getMemo())
                    .setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(dto.getCreatedBy())
                    .setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(dto.getUpdatedBy())
                    .setProductCategoryCid(dto.getProductCategoryCid());
            productEntities.add(productEntity);
        }
        return productEntities;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product id 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productId
     * @see ProductRepository
     */
    public void destroyOne(Integer productId) {
        productRepository.findById(productId).ifPresent(product -> {
            productRepository.delete(product);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param productDto
     * @param userId
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
     * Product id 값과 상응되는 데이터의 일부분을 업데이트한다.
     * 
     * @param productDto
     * @param userId
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
            if (productDto.getDefaultName() != null) {
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
