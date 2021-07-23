package com.piaar_store_manager.server.service.product;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product.dto.ProductCreateReqDto;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import com.piaar_store_manager.server.model.product.repository.ProductRepository;
import com.piaar_store_manager.server.model.product_option.repository.ProductOptionRepository;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;

import com.piaar_store_manager.server.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DateHandler dateHandler;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductOptionService productOptionService;

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
            productDto = getDtoByEntitiy(productEntityOpt.get());
        }else{
            throw new NullPointerException();
        }

        return productDto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductEntity => ProductGetDto
     * 
     * @param productEntity
     * @return ProductGetDto
     */
    private ProductGetDto getDtoByEntitiy(ProductEntity productEntity) {
        ProductGetDto productDto = new ProductGetDto();

        productDto.setCode(productEntity.getCode()).setManufacturingCode(productEntity.getManufacturingCode())
                .setNProductCode(productEntity.getNProductCode()).setDefaultName(productEntity.getDefaultName())
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
        List<ProductGetDto> productDto = getDtoByEntities(productEntities);

        return productDto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * List::ProductEntity:: => List::ProductGetDto::
     * 
     * @param productEntities
     * @return List::ProductGetDto::
     */
    private List<ProductGetDto> getDtoByEntities(List<ProductEntity> productEntities) {
        List<ProductGetDto> productDtos = new ArrayList<>();

        for (ProductEntity productEntity : productEntities) {
            ProductGetDto productDto = new ProductGetDto();

            productDto.setCode(productEntity.getCode()).setManufacturingCode(productEntity.getManufacturingCode())
                    .setNProductCode(productEntity.getNProductCode()).setDefaultName(productEntity.getDefaultName())
                    .setManagementName(productEntity.getManagementName()).setImageUrl(productEntity.getImageUrl())
                    .setImageFileName(productEntity.getImageFileName()).setMemo(productEntity.getMemo())
                    .setCreatedAt(productEntity.getCreatedAt()).setCreatedBy(productEntity.getCreatedBy())
                    .setUpdatedAt(productEntity.getUpdatedAt()).setUpdatedBy(productEntity.getUpdatedBy())
                    .setProductCategoryCid(productEntity.getProductCategoryCid());

            productDtos.add(productDto);
        }
        return productDtos;
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
        ProductEntity entity = convEntityByDto(productCreateReqDto.getProductDto(), userId);
        ProductEntity savedProductEntity = productRepository.save(entity);

        productOptionService.createList(productCreateReqDto.getOptionDtos(), userId, savedProductEntity.getCid());
    }

    private ProductEntity convEntityByDto(ProductGetDto productGetDto, UUID userId){
        ProductEntity productEntity = new ProductEntity();

        productEntity.setId(productGetDto.getId())
                     .setCode(productGetDto.getCode())
                     .setManufacturingCode(productGetDto.getManufacturingCode())
                     .setNProductCode(productGetDto.getNProductCode())
                     .setDefaultName(productGetDto.getDefaultName())
                     .setManagementName(productGetDto.getManagementName())
                     .setImageUrl(productGetDto.getImageUrl())
                     .setImageFileName(productGetDto.getImageFileName())
                     .setMemo(productGetDto.getMemo()).setCreatedAt(dateHandler.getCurrentDate())
                     .setCreatedBy(userId)
                     .setUpdatedAt(dateHandler.getCurrentDate())
                     .setUpdatedBy(userId)
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
                .setManufacturingCode(productDto.getManufacturingCode()).setNProductCode(productDto.getNProductCode())
                .setDefaultName(productDto.getDefaultName()).setManagementName(productDto.getManagementName())
                .setImageUrl(productDto.getImageUrl()).setImageFileName(productDto.getImageFileName())
                .setMemo(productDto.getMemo()).setCreatedAt(dateHandler.getCurrentDate())
                .setCreatedBy(userId)
                .setUpdatedAt(dateHandler.getCurrentDate())
                .setUpdatedBy(userId)
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

        for(ProductCreateReqDto dto : productCreateReqDtos){
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
                    .setManufacturingCode(dto.getManufacturingCode()).setNProductCode(dto.getNProductCode())
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
                    .setNProductCode(productDto.getNProductCode()).setDefaultName(productDto.getDefaultName())
                    .setManagementName(productDto.getManagementName()).setImageUrl(productDto.getImageUrl())
                    .setImageFileName(productDto.getImageFileName()).setMemo(productDto.getMemo())
                    .setUpdatedAt(dateHandler.getCurrentDate())
                    .setUpdatedBy(userId)
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
            if (productDto.getNProductCode() != null) {
                productEntity.setNProductCode(productDto.getNProductCode());
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
