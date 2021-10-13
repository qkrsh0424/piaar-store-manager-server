package com.piaar_store_manager.server.service.product_release;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseJoinResDto;
import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.model.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.model.product_release.repository.ProductReleaseRepository;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;
import com.piaar_store_manager.server.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductReleaseService {

    @Autowired
    private ProductReleaseRepository productReleaseRepository;

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private DateHandler dateHandler;

    @Autowired
    private UserService userService;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터를 조회한다.
     *
     * @param productReleaseId : Integer
     * @return ProductReleaseGetDto
     * @see ProductReleaseRepository#findById
     * @see ProductReleaseGetDto#toDto
     */
    public ProductReleaseGetDto searchOne(Integer productReleaseId) {
        Optional<ProductReleaseEntity> productEntityOpt = productReleaseRepository.findById(productReleaseId);
        ProductReleaseGetDto dto = new ProductReleaseGetDto();

        if (productEntityOpt.isPresent()) {
            dto = ProductReleaseGetDto.toDto(productEntityOpt.get());
        } else {
            throw new NullPointerException();
        }

        return dto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터를 조회한다.
     * 해당 ProductRelease와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @param productReleaseId : Integer
     * @return ProductReleaseJoinResDto
     * @see ProductReleaseRepository#selectByCid
     * @see ProductOptionGetDto#toDto
     * @see ProductGetDto#toDto
     * @see ProductCategoryGetDto#toDto
     * @see UserService#getDtoByEntity
     */
    public ProductReleaseJoinResDto searchOneM2OJ(Integer productReleaseId){
        ProductReleaseJoinResDto productReleaseResDto = new ProductReleaseJoinResDto();

        Optional<ProductReleaseProj> productReleaseProjOpt = productReleaseRepository.selectByCid(productReleaseId);

        if(productReleaseProjOpt.isPresent()) {
            ProductReleaseGetDto productReleaseDto = ProductReleaseGetDto.toDto(productReleaseProjOpt.get().getProductRelease());
            ProductOptionGetDto productOptionDto = ProductOptionGetDto.toDto(productReleaseProjOpt.get().getProductOption());
            ProductGetDto productDto = ProductGetDto.toDto(productReleaseProjOpt.get().getProduct());
            ProductCategoryGetDto productCategoryDto = ProductCategoryGetDto.toDto(productReleaseProjOpt.get().getCategory());
            UserGetDto userDto = userService.getDtoByEntity(productReleaseProjOpt.get().getUser());

            productReleaseResDto.setRelease(productReleaseDto)
                                .setOption(productOptionDto)
                                .setProduct(productDto)
                                .setCategory(productCategoryDto)
                                .setUser(userDto);

        } else {
            throw new NullPointerException();
        }
            
        return productReleaseResDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductRelease 데이터를 모두 조회한다.
     * 
     * @return List::ProductReleaseGetDto::
     * @see ProductReleaseRepository#findAll
     * @see ProductReleaseGetDto#toDto
     */
    public List<ProductReleaseGetDto> searchList() {
        List<ProductReleaseEntity> entities = productReleaseRepository.findAll();
        List<ProductReleaseGetDto> dtos = new ArrayList<>();

        for(ProductReleaseEntity entity : entities){
            dtos.add(ProductReleaseGetDto.toDto(entity));
        }
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 ProductRelease 데이터를 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReceiveGetDto
     * @see ProductReceiveRepository#findByProductOptionCid
     * @see ProductReleaseGetDto#toDto
     */
    public List<ProductReleaseGetDto> searchList(Integer productOptionCid) {
        List<ProductReleaseEntity> productEntities = productReleaseRepository.findByProductOptionCid(productOptionCid);
        List<ProductReleaseGetDto> dtos = new ArrayList<>();

        for(ProductReleaseEntity entity : productEntities){
            dtos.add(ProductReleaseGetDto.toDto(entity));
        }

        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductRelease 데이터를 모두 조회한다.
     * 해당 ProductRelease와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return List::ProductReleaseJoinResDto::
     * @see ProductReleaseRepository#selectAll
     */
    public List<ProductReleaseJoinResDto> searchListM2OJ() {
        List<ProductReleaseJoinResDto> productReleaseJoinResDtos = new ArrayList<>();
        List<ProductReleaseProj> productReleaseProjs = productReleaseRepository.selectAll();
        
        for (ProductReleaseProj projReleaseOpt : productReleaseProjs) {
            productReleaseJoinResDtos.add(searchOneM2OJ(projReleaseOpt.getProductRelease().getCid()));
        }
        return productReleaseJoinResDtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductRelease 내용을 한개 등록한다.
     * 상품 출고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReleaseGetDto : ProductReleaseGetDto
     * @param userId : UUID
     * @return ProductReleaseEntity
     * @see ProductReleaseEntity#toEntity
     * @see ProductReleaseRepository#save
     */
    public ProductReleaseEntity createPR(ProductReleaseGetDto productReleaseGetDto, UUID userId) {
        productReleaseGetDto.setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId);

        ProductReleaseEntity entity = ProductReleaseEntity.toEntity(productReleaseGetDto);
        productReleaseRepository.save(entity);

        return entity;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductRelease 내용을 여러개 등록한다.
     * 상품 출고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReleaseGetDtos : List::ProductReleaseGetDto::
     * @param userId : UUID
     * @return List::ProductReleaseEntity::
     * @see ProductReleaseEntity#toEntity
     * @see ProductReleaseRepository#saveAll
     */
    public List<ProductReleaseEntity> createPRList(List<ProductReleaseGetDto> productReleaseGetDtos, UUID userId) {
        List<ProductReleaseEntity> entities = new ArrayList<>();
        
        for(ProductReleaseGetDto dto : productReleaseGetDtos) {
            dto.setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId);

            ProductReleaseEntity entity = ProductReleaseEntity.toEntity(dto);
            entities.add(entity);
        }

        productReleaseRepository.saveAll(entities);
        return entities;
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productReleaseCid : Integer
     * @param userId : UUID
     * @see ProductReleaseRepository#findById
     * @see ProductOptionService#releaseProductUnit
     * @see ProductReleaseRepository#delete
     */
    public void destroyOne(Integer productReleaseCid, UUID userId) {
        productReleaseRepository.findById(productReleaseCid).ifPresent(releaseProduct -> {
            productOptionService.releaseProductUnit(releaseProduct.getProductOptionCid(), userId, releaseProduct.getReleaseUnit());
            productReleaseRepository.delete(releaseProduct);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param releaseDto : ProductReleaseGetDto
     * @param userId : UUID
     * @see ProductReleaseRepository#findById
     * @see ProductOptionService#receiveProductUnit
     * @see ProductReleaseRepository#save
     * @see ProductOptionService#releaseProductUnit
     */
    public void changeOne(ProductReleaseGetDto releaseDto, UUID userId) {
        productReleaseRepository.findById(releaseDto.getCid()).ifPresentOrElse(changeEntity -> {
            
            // 현재 저장된 출고 데이터 삭제
            productOptionService.receiveProductUnit(changeEntity.getProductOptionCid(), userId, changeEntity.getReleaseUnit());

            // 변경된 출고 데이터 저장
            changeEntity.setCid(releaseDto.getCid())
                        .setReleaseUnit(releaseDto.getReleaseUnit())
                        .setMemo(releaseDto.getMemo())
                        .setProductOptionCid(releaseDto.getProductOptionCid());
            
            productReleaseRepository.save(changeEntity);
            productOptionService.releaseProductUnit(changeEntity.getProductOptionCid(), userId, changeEntity.getReleaseUnit());
        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 각 상품마다 ProductRelease cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param productReleaseGetDtos : List::ProductReleaseGetDto::
     * @param userId :: UUID
     */
    public void changeList(List<ProductReleaseGetDto> productReleaseGetDtos, UUID userId) {
        for(ProductReleaseGetDto dto : productReleaseGetDtos) {
            changeOne(dto, userId);
        }
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductRelease cid 값과 상응되는 데이터의 일부분을 업데이트한다.
     * 
     * @param releaseDto : ProductReleaseGetDto
     * @param userId : UUID
     * @see ProductReleaseRepository#findById
     * @see ProductOptionService#receiveProductUnit
     * @see ProductReleaseRepository#save
     * @see ProductOptionService#releaseProductUnit
     */
    public void patchOne(ProductReleaseGetDto releaseDto, UUID userId) {
        productReleaseRepository.findById(releaseDto.getCid()).ifPresentOrElse(releaseEntity -> {
            if(releaseDto.getReleaseUnit() != null){
                // 현재 저장된 출고 상품 개수 삭제
                productOptionService.receiveProductUnit(releaseEntity.getProductOptionCid(), userId, releaseEntity.getReleaseUnit());

                // 변경된 출고 상품 개수 저장
                releaseEntity.setReleaseUnit(releaseDto.getReleaseUnit());
                productReleaseRepository.save(releaseEntity);
                productOptionService.releaseProductUnit(releaseEntity.getProductOptionCid(), userId, releaseEntity.getReleaseUnit());
            }
            if(releaseDto.getMemo() != null) {
                releaseEntity.setMemo(releaseDto.getMemo());
            }
            if(releaseDto.getProductOptionCid() != null) {
                // 현재 저장된 출고 상품 개수 삭제
                productOptionService.receiveProductUnit(releaseEntity.getProductOptionCid(), userId, releaseEntity.getReleaseUnit());

                // 변경된 출고 상품 개수 및 데이터 저장
                releaseEntity.setProductOptionCid(releaseDto.getProductOptionCid());
                productReleaseRepository.save(releaseEntity);
                productOptionService.releaseProductUnit(releaseEntity.getProductOptionCid(), userId, releaseEntity.getReleaseUnit());
            }
        }, null);
    }
}
