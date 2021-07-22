package com.piaar_store_manager.server.service.product;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product.dto.ProductCreateReqDto;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import com.piaar_store_manager.server.model.product.repository.ProductRepository;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.model.product_option.repository.ProductOptionRepository;
import com.piaar_store_manager.server.model.user.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product id 값과 상응되는 데이터를 조회한다.
     *
     * @param productId
     * @return ProductGetDto
     * @see ProductRepository
     */
    public ProductGetDto searchOne(Integer productId){
        Optional<ProductEntity> productEntityOpt = productRepository.findById(productId);
        ProductGetDto productDto = new ProductGetDto();

        if(productEntityOpt.isPresent()){
            productDto = getDtoByEntitiy(productEntityOpt.get());
        }

        return productDto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductEntity => ProductGetDto
     * @param productEntity
     * @return ProductGetDto
     */
    private ProductGetDto getDtoByEntitiy(ProductEntity productEntity){
        ProductGetDto productDto = new ProductGetDto();

        productDto.setCode(productEntity.getCode())
                .setManufacturingCode(productEntity.getManufacturingCode())
                .setNProductCode(productEntity.getNProductCode())
                .setDefaultName(productEntity.getDefaultName())
                .setManagementName(productEntity.getManagementName())
                .setImageUrl(productEntity.getImageUrl())
                .setImageFileName(productEntity.getImageFileName())
                .setMemo(productEntity.getMemo())
                .setCreatedAt(productEntity.getCreatedAt())
                .setCreatedBy(productEntity.getCreatedBy())
                .setUpdatedAt(productEntity.getUpdatedAt())
                .setUpdatedBy(productEntity.getUpdatedBy())
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
    public List<ProductGetDto> searchList(){
        List<ProductEntity> productEntities = productRepository.findAll();
        List<ProductGetDto> productDto = getDtoByEntities(productEntities);

        return productDto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * List::ProductEntity:: => List::ProductGetDto::
     * @param productEntities
     * @return List::ProductGetDto::
     */
    private List<ProductGetDto> getDtoByEntities(List<ProductEntity> productEntities){
        List<ProductGetDto> productDtos = new ArrayList<>();

        for(ProductEntity productEntity : productEntities) {
            ProductGetDto productDto = new ProductGetDto();

            productDto.setCode(productEntity.getCode())
                    .setManufacturingCode(productEntity.getManufacturingCode())
                    .setNProductCode(productEntity.getNProductCode())
                    .setDefaultName(productEntity.getDefaultName())
                    .setManagementName(productEntity.getManagementName())
                    .setImageUrl(productEntity.getImageUrl())
                    .setImageFileName(productEntity.getImageFileName())
                    .setMemo(productEntity.getMemo())
                    .setCreatedAt(productEntity.getCreatedAt())
                    .setCreatedBy(productEntity.getCreatedBy())
                    .setUpdatedAt(productEntity.getUpdatedAt())
                    .setUpdatedBy(productEntity.getUpdatedBy())
                    .setProductCategoryCid(productEntity.getProductCategoryCid());

            productDtos.add(productDto);
        }
        return productDtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product 내용을 한개 등록한다.
     * @param productGetDto
     * @param userId
     * @see ProductRepository
     */
    public void createOne(ProductGetDto productGetDto, UUID userId){
        ProductEntity entity = convEntitiyByDto(productGetDto, userId);
        productRepository.save(entity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product와 ProductOption 내용을 같이 등록한다.
     * @param productCreateReqDto
     * @param userId
     */
    public void createPAO(ProductCreateReqDto productCreateReqDto, UUID userId){
        ProductEntity entity = convEntitiyByDto(productCreateReqDto, userId);
        productRepository.save(entity);
    }

    private ProductEntity convEntitiyByDto(ProductCreateReqDto productCreateReqDto, UUID userId){
        ProductEntity productEntity = new ProductEntity();
        ProductOptionEntity productOptionEntity = new ProductOptionEntity();

        productEntity.setId(productCreateReqDto.getProductDto().getId())
                .setCode(productCreateReqDto.getProductDto().getCode())
                .setManufacturingCode(productCreateReqDto.getProductDto().getManufacturingCode())
                .setNProductCode(productCreateReqDto.getProductDto().getNProductCode())
                .setDefaultName(productCreateReqDto.getProductDto().getDefaultName())
                .setManagementName(productCreateReqDto.getProductDto().getManagementName())
                .setImageUrl(productCreateReqDto.getProductDto().getImageUrl())
                .setImageFileName(productCreateReqDto.getProductDto().getImageFileName())
                .setMemo(productCreateReqDto.getProductDto().getMemo())
                .setCreatedAt(dateHandler.getCurrentDate())
                .setCreatedBy(userRepository.findById(userId).get().getName())
                .setUpdatedAt(dateHandler.getCurrentDate())
                .setUpdatedBy(userRepository.findById(userId).get().getName())
                .setProductCategoryCid(productCreateReqDto.getProductDto().getProductCategoryCid());

        productRepository.save(productEntity);

        if(productEntity.getId().equals(productCreateReqDto.getOptionDtos().get(0).getProductId())) {
           productOptionEntity.setId(productCreateReqDto.getOptionDtos().get(0).getId())
                        .setDefaultName(productCreateReqDto.getOptionDtos().get(0).getDefaultName())
                        .setManagementName(productCreateReqDto.getOptionDtos().get(0).getManagementName())
                        .setSalesPrice(productCreateReqDto.getOptionDtos().get(0).getSalesPrice())
                        .setStockUnit(productCreateReqDto.getOptionDtos().get(0).getStockUnit())
                        .setStatus(productCreateReqDto.getOptionDtos().get(0).getStatus())
                        .setMemo(productCreateReqDto.getOptionDtos().get(0).getMemo())
                        .setCreatedAt(dateHandler.getCurrentDate())
                        .setCreatedBy(userRepository.findById(userId).get().getName())
                        .setUpdatedAt(dateHandler.getCurrentDate())
                        .setUpdatedBy(userRepository.findById(userId).get().getName())
                        .setProductCid(productEntity.getCid());

           productOptionRepository.save(productOptionEntity);
        }
        return productEntity;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductGetDto => ProductEntity
     * @param productDto
     * @param userId
     * @return ProductEntity
     */
    private ProductEntity convEntitiyByDto(ProductGetDto productDto, UUID userId){
        ProductEntity productEntity = new ProductEntity();

        productEntity.setId(UUID.randomUUID())
                    .setCode(productDto.getCode())
                    .setManufacturingCode(productDto.getManufacturingCode())
                    .setNProductCode(productDto.getNProductCode())
                    .setDefaultName(productDto.getDefaultName())
                    .setManagementName(productDto.getManagementName())
                    .setImageUrl(productDto.getImageUrl())
                    .setImageFileName(productDto.getImageFileName())
                    .setMemo(productDto.getMemo())
                    .setCreatedAt(dateHandler.getCurrentDate())
                    .setCreatedBy(userRepository.findById(userId).get().getName())
                    .setUpdatedAt(dateHandler.getCurrentDate())
                    .setUpdatedBy(userRepository.findById(userId).get().getName())
                    .setProductCategoryCid(productDto.getProductCategoryCid());

        return productEntity;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product 내용을 여러개 등록한다.
     * @param productGetDtos
     * @param userId
     * @see ProductRepository
     */
    public void createList(List<ProductGetDto> productGetDtos, UUID userId){
        List<ProductEntity> entites = convEntitiesByDtos(productGetDtos, userId);
        productRepository.saveAll(entites);
    }

    public void createPAOList(List<ProductCreateReqDto> productCreateReqDtos, UUID userId){
        List<ProductEntity> entites = convEntitiesByReqDtos(productCreateReqDtos, userId);
        productRepository.saveAll(entites);
    }

    private List<ProductEntity> convEntitiesByReqDtos(List<ProductCreateReqDto> productCreateReqDtos, UUID userId){
        List<ProductEntity> productEntities = new ArrayList<>();
        List<ProductOptionEntity> productOptionEntities = new ArrayList<>();

        for(ProductCreateReqDto dto : productCreateReqDtos){
            ProductEntity entity = new ProductEntity();
            entity.setId(dto.getProductDto().getId())
                    .setCode(dto.getProductDto().getCode())
                    .setManufacturingCode(dto.getProductDto().getManufacturingCode())
                    .setNProductCode(dto.getProductDto().getNProductCode())
                    .setDefaultName(dto.getProductDto().getDefaultName())
                    .setManagementName(dto.getProductDto().getManagementName())
                    .setImageUrl(dto.getProductDto().getImageUrl())
                    .setImageFileName(dto.getProductDto().getImageFileName())
                    .setMemo(dto.getProductDto().getMemo())
                    .setCreatedAt(dateHandler.getCurrentDate())
                    .setCreatedBy(userRepository.findById(userId).get().getName())
                    .setUpdatedAt(dateHandler.getCurrentDate())
                    .setUpdatedBy(userRepository.findById(userId).get().getName())
                    .setProductCategoryCid(dto.getProductDto().getProductCategoryCid());

            productRepository.save(entity);

            int i = 0;
            for(ProductCreateReqDto reqDto : productCreateReqDtos){
                ProductOptionEntity optionEntity = new ProductOptionEntity();

                if(entity.getId().equals(reqDto.getOptionDtos().get(i).getProductId())) {
                    optionEntity.setId(reqDto.getOptionDtos().get(i).getId())
                            .setDefaultName(reqDto.getOptionDtos().get(i).getDefaultName())
                            .setManagementName(reqDto.getOptionDtos().get(i).getManagementName())
                            .setSalesPrice(reqDto.getOptionDtos().get(i).getSalesPrice())
                            .setStockUnit(reqDto.getOptionDtos().get(i).getStockUnit())
                            .setStatus(reqDto.getOptionDtos().get(i).getStatus())
                            .setMemo(reqDto.getOptionDtos().get(i).getMemo())
                            .setCreatedAt(dateHandler.getCurrentDate())
                            .setCreatedBy(userRepository.findById(userId).get().getName())
                            .setUpdatedAt(dateHandler.getCurrentDate())
                            .setUpdatedBy(userRepository.findById(userId).get().getName())
                            .setProductCid(entity.getCid());

                    productOptionEntities.add(optionEntity);
                }
                i++;
                productOptionRepository.saveAll(productOptionEntities);
            }
        }

        return productEntities;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * List::ProductGetDto:: => List::ProductEntity::
     * @param productGetDtos
     * @param userId
     * @return List::ProductEntity::
     */
    private List<ProductEntity> convEntitiesByDtos(List<ProductGetDto> productGetDtos, UUID userId){
        List<ProductEntity> productEntities = new ArrayList<>();

        for(ProductGetDto dto : productGetDtos) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setId(UUID.randomUUID())
                        .setCode(dto.getCode())
                        .setManufacturingCode(dto.getManufacturingCode())
                        .setNProductCode(dto.getNProductCode())
                        .setDefaultName(dto.getDefaultName())
                        .setManagementName(dto.getManagementName())
                        .setImageUrl(dto.getImageUrl())
                        .setImageFileName(dto.getImageFileName())
                        .setMemo(dto.getMemo())
                        .setCreatedAt(dateHandler.getCurrentDate())
                        .setCreatedBy(dto.getCreatedBy())
                        .setUpdatedAt(dateHandler.getCurrentDate())
                        .setUpdatedBy(dto.getUpdatedBy())
                        .setProductCategoryCid(dto.getProductCategoryCid());
            productEntities.add(productEntity);
        }
        return productEntities;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product id 값과 상응되는 데이터를 삭제한다.
     * @param productId
     * @see ProductRepository
     */
    public void destroyOne(Integer productId){
        productRepository.findById(productId).ifPresent(product-> {
            productRepository.delete(product);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 업데이트한다.
     * @param productDto
     * @param userId
     */
    public void changeOne(ProductGetDto productDto, UUID userId){
        productRepository.findById(productDto.getCid()).ifPresentOrElse(productEntity -> {
            productEntity.setCode(productDto.getCode())
                        .setManufacturingCode(productDto.getManufacturingCode())
                        .setNProductCode(productDto.getNProductCode())
                        .setDefaultName(productDto.getDefaultName())
                        .setManagementName(productDto.getManagementName())
                        .setImageUrl(productDto.getImageUrl())
                        .setImageFileName(productDto.getImageFileName())
                        .setMemo(productDto.getMemo())
                        .setUpdatedAt(dateHandler.getCurrentDate())
                        .setUpdatedBy(userRepository.findById(userId).get().getName())
                        .setProductCategoryCid(productDto.getProductCategoryCid());

            productRepository.save(productEntity);
        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * Product id 값과 상응되는 데이터의 일부분을 업데이트한다.
     * @param productDto
     * @param userId
     */
    public void patchOne(ProductGetDto productDto, UUID userId){
        productRepository.findById(productDto.getCid()).ifPresentOrElse(productEntity -> {
            if(productDto.getCode() != null){
                productEntity.setCode(productDto.getCode());
            }
            else if(productDto.getManufacturingCode() != null) {
                productEntity.setManufacturingCode(productDto.getManufacturingCode());
            }
            else if(productDto.getNProductCode() != null) {
                productEntity.setNProductCode(productDto.getNProductCode());
            }
            else if(productDto.getDefaultName() != null) {
                productEntity.setDefaultName(productDto.getDefaultName());
            }
            else if(productDto.getManagementName() != null) {
                productEntity.setManagementName(productDto.getManagementName());
            }
            else if(productDto.getDefaultName() != null) {
                productEntity.setImageUrl(productDto.getImageUrl());
            }
            else if(productDto.getImageFileName() != null) {
                productEntity.setImageFileName(productDto.getImageFileName());
            }
            else if(productDto.getMemo() != null) {
                productEntity.setMemo(productDto.getMemo());
            }
            else if(productDto.getProductCategoryCid() != null) {
                productEntity.setProductCategoryCid(productDto.getProductCategoryCid());
            }

            productEntity.setUpdatedAt(dateHandler.getCurrentDate())
                         .setUpdatedBy(userRepository.findById(userId).get().getName());

            productRepository.save(productEntity);
        }, null);
    }

}
