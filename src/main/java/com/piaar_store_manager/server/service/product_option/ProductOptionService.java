package com.piaar_store_manager.server.service.product_option;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionJoinResDto;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.model.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.model.product_option.repository.ProductOptionRepository;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;
import com.piaar_store_manager.server.service.product.ProductService;
import com.piaar_store_manager.server.service.product_category.ProductCategoryService;

import com.piaar_store_manager.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductOptionService {

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private DateHandler dateHandler;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    UserService userService;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption id 값과 상응되는 데이터를 조회한다.
     *
     * @param productOptionId
     * @return ProductOptionGetDto
     * @see ProductOptionRepository
     */
    public ProductOptionGetDto searchOne(Integer productOptionId) {
        Optional<ProductOptionEntity> productOptionEntityOpt = productOptionRepository.findById(productOptionId);
        ProductOptionGetDto productOptionDto = new ProductOptionGetDto();

        if (productOptionEntityOpt.isPresent()) {
            productOptionDto = getDtoByEntity(productOptionEntityOpt.get());
        } else {
            throw new NullPointerException();
        }

        return productOptionDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption id 값과 상응되는 데이터를 조회한다. 해당 ProductOption와 연관관계에 놓여있는 One To Many
     * JOIN(o2mj) 상태를 조회한다.
     * 
     *
     * @param productOptionId
     * @return ProductOptionGetDto
     * @see ProductOptionRepository#selectByCid
     * @see ProductEntity
     * @see UserEntity
     * @see ProductCategoryEntity
     * 
     * @return ProductOptionJoinResDto
     */
    public ProductOptionJoinResDto searchOneO2MJ(Integer productOptionId) {
        ProductOptionJoinResDto productOptionResDto = new ProductOptionJoinResDto();

        Optional<ProductOptionProj> productOptionProjOpt = productOptionRepository.selectByCid(productOptionId);

        if (productOptionProjOpt.isPresent()) {
            ProductGetDto productGetDto = productService.getDtoByEntity(productOptionProjOpt.get().getProduct());
            UserGetDto userGetDto = userService.getDtoByEntity(productOptionProjOpt.get().getUser());
            ProductCategoryGetDto categoryGetDto = productCategoryService.getDtoByEntity(productOptionProjOpt.get().getCategory());
            ProductOptionGetDto productOptionGetDto = this.getDtoByEntity(productOptionProjOpt.get().getProductOption());

            productOptionResDto.setProduct(productGetDto).setUser(userGetDto).setCategory(categoryGetDto)
                    .setOption(productOptionGetDto);
        } else {
            throw new NullPointerException();
        }
        return productOptionResDto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * List::ProductOptionEntity:: => List::ProductOptionGetDto::
     * 
     * @param productOptionEntity
     * @return ProductOptionGetDto
     */
    public ProductOptionGetDto getDtoByEntity(ProductOptionEntity productOptionEntity) {
        ProductOptionGetDto productOptionDto = new ProductOptionGetDto();

        productOptionDto
                .setCid(productOptionEntity.getCid())
                .setId(productOptionEntity.getId())
                .setDefaultName(productOptionEntity.getDefaultName())
                .setManagementName(productOptionEntity.getManagementName())
                .setSalesPrice(productOptionEntity.getSalesPrice())
                .setStockUnit(productOptionEntity.getStockUnit())
                .setStatus(productOptionEntity.getStatus())
                .setMemo(productOptionEntity.getMemo())
                .setCreatedAt(productOptionEntity.getCreatedAt())
                .setCreatedBy(productOptionEntity.getCreatedBy())
                .setUpdatedAt(productOptionEntity.getUpdatedAt())
                .setUpdatedBy(productOptionEntity.getUpdatedBy())
                .setProductCid(productOptionEntity.getProductCid());

        return productOptionDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 상품옵션들을 모두 조회한다.
     *
     * @return List::ProductOptionDto::
     * @see ProductOptionRepository
     */
    public List<ProductOptionGetDto> searchList() {
        List<ProductOptionEntity> productOptionEntities = productOptionRepository.findAll();
        List<ProductOptionGetDto> productOptionDto = new ArrayList<>();

        for (ProductOptionEntity optionEntity : productOptionEntities) {
            productOptionDto.add(getDtoByEntity(optionEntity));
        }
        return productOptionDto;
    }

    // TODO(READ) :: ADD NEW
    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption 데이터를 모두 조회한다. 해당 ProductOption와 연관관계에 놓여있는 One To Many
     * JOIN(o2mj) 상태를 조회한다.
     *
     *
     * @return List::ProductOptionJoinResDto::
     * @see ProductOptionRepository#selectAll
     * @see ProductEntity
     * @see UserEntity
     * @see ProductCategoryEntity
     *
     * @return ProductOptionJoinResDto
     */
    public List<ProductOptionJoinResDto> searchListO2MJ() {
        List<ProductOptionJoinResDto> productOptionJoinResDtos = new ArrayList<>();
        List<ProductOptionProj> productOptionProjs = productOptionRepository.selectAll();
        
        for (ProductOptionProj projOptionOpt : productOptionProjs) {
            productOptionJoinResDtos.add(searchOneO2MJ(projOptionOpt.getProductOption().getCid()));
        }
        return productOptionJoinResDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 상품옵션들중 product cid와 대응되는 값을 모두 조회한다.
     *
     * @return List::ProductOptionDto::
     * @see ProductOptionRepository
     */
    public List<ProductOptionGetDto> searchList(Integer productCid) {
        List<ProductOptionEntity> productOptionEntities = productOptionRepository.findAll(productCid);
        List<ProductOptionGetDto> productOptionDto = new ArrayList<>();

        for (ProductOptionEntity optionEntity : productOptionEntities) {
            productOptionDto.add(getDtoByEntity(optionEntity));
        }

        return productOptionDto;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductOption 내용을 한개 등록한다.
     * 
     * @param productOptionGetDto
     * @param userId
     * @see ProductOptionRepository
     */
    public void createOne(ProductOptionGetDto productOptionGetDto, UUID userId) {
        ProductOptionEntity entity = convEntitiyByDto(productOptionGetDto, userId, productOptionGetDto.getProductCid());
        productOptionRepository.save(entity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductOption 내용을 한개 등록한다.
     * 
     * @param productOptionGetDto
     * @param userId
     * @param productCid
     * @see ProductOptionRepository
     */
    public void createOne(ProductOptionGetDto productOptionGetDto, UUID userId, Integer productCid) {
        ProductOptionEntity entity = convEntitiyByDto(productOptionGetDto, userId, productCid);
        productOptionRepository.save(entity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductOption 내용을 여러개 등록한다.
     * 
     * @param productOptionGetDto
     * @param userId
     * @param productCid
     * @see ProductOptionRepository
     */
    public void createList(List<ProductOptionGetDto> productOptionGetDtos, UUID userId, Integer productCid) {
        List<ProductOptionEntity> entities = new ArrayList<>();

        for (ProductOptionGetDto dto : productOptionGetDtos) {
            ProductOptionEntity entity = convEntitiyByDto(dto, userId, productCid);
            entities.add(entity);
        }
        productOptionRepository.saveAll(entities);
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductOptionGetDto => ProductOptionEntity
     * 
     * @param productOptionDto
     * @param userId
     * @return ProductOptionEntity
     */
    private ProductOptionEntity convEntitiyByDto(ProductOptionGetDto productOptionDto, UUID userId, Integer productCid) {
        ProductOptionEntity productOptionEntity = new ProductOptionEntity();

        productOptionEntity.setId(UUID.randomUUID()).setDefaultName(productOptionDto.getDefaultName())
                .setManagementName(productOptionDto.getManagementName()).setSalesPrice(productOptionDto.getSalesPrice())
                .setStockUnit(productOptionDto.getStockUnit()).setStatus(productOptionDto.getStatus())
                .setMemo(productOptionDto.getMemo()).setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId)
                .setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId).setProductCid(productCid);

        return productOptionEntity;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption id 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productOptionId
     * @see ProductOptionRepository
     */
    public void destroyOne(Integer productOptionId) {
        productOptionRepository.findById(productOptionId).ifPresent(productOption -> {
            productOptionRepository.delete(productOption);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param productOptionDto
     * @param userId
     */
    public void changeOne(ProductOptionGetDto productOptionDto, UUID userId) {
        productOptionRepository.findById(productOptionDto.getCid()).ifPresentOrElse(productOptionEntity -> {
            productOptionEntity.setDefaultName(productOptionDto.getDefaultName())
                    .setManagementName(productOptionDto.getManagementName())
                    .setSalesPrice(productOptionDto.getSalesPrice()).setStockUnit(productOptionDto.getStockUnit())
                    .setStatus(productOptionDto.getStatus()).setMemo(productOptionDto.getMemo())
                    .setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId)
                    .setProductCid(productOptionDto.getProductCid());

            productOptionRepository.save(productOptionEntity);
        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductOption id 값과 상응되는 데이터의 일부분을 업데이트한다.
     * 
     * @param productOptionDto
     * @param userId
     */
    public void patchOne(ProductOptionGetDto productOptionDto, UUID userId) {
        productOptionRepository.findById(productOptionDto.getCid()).ifPresentOrElse(productOptionEntity -> {
            if (productOptionDto.getDefaultName() != null) {
                productOptionEntity.setDefaultName(productOptionDto.getDefaultName());
            }
            if (productOptionDto.getManagementName() != null) {
                productOptionEntity.setManagementName(productOptionDto.getManagementName());
            }
            if (productOptionDto.getSalesPrice() != null) {
                productOptionEntity.setSalesPrice(productOptionDto.getSalesPrice());
            }
            if (productOptionDto.getStockUnit() != null) {
                productOptionEntity.setStockUnit(productOptionDto.getStockUnit());
            }
            if (productOptionDto.getStatus() != null) {
                productOptionEntity.setStatus(productOptionDto.getStatus());
            }
            if (productOptionDto.getMemo() != null) {
                productOptionEntity.setMemo(productOptionDto.getMemo());
            }
            if (productOptionDto.getProductCid() != null) {
                productOptionEntity.setProductCid(productOptionDto.getProductCid());
            }

            productOptionEntity.setUpdatedAt(dateHandler.getCurrentDate()).setUpdatedBy(userId);

            productOptionRepository.save(productOptionEntity);
        }, null);
    }

    public void receiveProductUnit(Integer optionCid, UUID userId, Integer receiveUnit){

        productOptionRepository.findById(optionCid).ifPresentOrElse(productOptionEntity -> {
            productOptionEntity.setStockUnit(productOptionEntity.getStockUnit() + receiveUnit)
                               .setUpdatedAt(dateHandler.getCurrentDate())
                               .setUpdatedBy(userId);

            productOptionRepository.save(productOptionEntity);
        }, null);
    }

    public void releaseProductUnit(Integer optionCid, UUID userId, Integer receiveUnit){

        productOptionRepository.findById(optionCid).ifPresentOrElse(productOptionEntity -> {
            productOptionEntity.setStockUnit(productOptionEntity.getStockUnit() - receiveUnit)
                               .setUpdatedAt(dateHandler.getCurrentDate())
                               .setUpdatedBy(userId);

            productOptionRepository.save(productOptionEntity);
        }, null);
    }
}
