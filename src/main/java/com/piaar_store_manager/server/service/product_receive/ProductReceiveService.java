package com.piaar_store_manager.server.service.product_receive;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveJoinResDto;
import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.model.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.model.product_receive.repository.ProductReceiveRepository;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;
import com.piaar_store_manager.server.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductReceiveService {
    
    @Autowired
    private ProductReceiveRepository productReceiveRepository;

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private DateHandler dateHandler;

    @Autowired
    private UserService userService;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터를 조회한다.
     *
     * @param productCid : Integer
     * @return ProductReceiveGetDto
     * @see ProductReceiveRepository#findById
     * @see ProductReceiveGetDto#toDto
     */
    public ProductReceiveGetDto searchOne(Integer productReceiveCid) {
        Optional<ProductReceiveEntity> productEntityOpt = productReceiveRepository.findById(productReceiveCid);
        ProductReceiveGetDto dto = new ProductReceiveGetDto();

        if (productEntityOpt.isPresent()) {
            dto = ProductReceiveGetDto.toDto(productEntityOpt.get());
        } else {
            throw new NullPointerException();
        }

        return dto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터를 조회한다.
     * 해당 ProductReceive와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @param productReceiveCid : Integer
     * @return ProductReceiveJoinResDto
     * @see ProductReceiveRepository#selectByCid
     * @see ProductReceiveGetDto#toDto
     * @see ProductOptionGetDto#toDto
     * @see ProductGetDto#toDto
     * @see ProductCategoryGetDto#toDto
     * @see UserService#getDtoByEntity
     */
    public ProductReceiveJoinResDto searchOneM2OJ(Integer productReceiveCid){
        ProductReceiveJoinResDto productReceiveResDto = new ProductReceiveJoinResDto();

        Optional<ProductReceiveProj> productReceiveProjOpt = productReceiveRepository.selectByCid(productReceiveCid);

        if(productReceiveProjOpt.isPresent()) {
            ProductReceiveGetDto productReceiveDto = ProductReceiveGetDto.toDto(productReceiveProjOpt.get().getProductReceive());
            ProductOptionGetDto productOptionDto = ProductOptionGetDto.toDto(productReceiveProjOpt.get().getProductOption());
            ProductGetDto productDto = ProductGetDto.toDto(productReceiveProjOpt.get().getProduct());
            ProductCategoryGetDto productCategoryDto = ProductCategoryGetDto.toDto(productReceiveProjOpt.get().getCategory());
            UserGetDto userDto = userService.getDtoByEntity(productReceiveProjOpt.get().getUser());

            productReceiveResDto.setReceive(productReceiveDto)
                                .setOption(productOptionDto)
                                .setProduct(productDto)
                                .setCategory(productCategoryDto)
                                .setUser(userDto);

        } else {
            throw new NullPointerException();
        }
            
        return productReceiveResDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive 데이터를 모두 조회한다.
     * 
     * @return List::ProductReceiveGetDto::
     * @see ProductReceiveRepository#findAll
     * @see ProductReceiveGetDto#toDto
     */
    public List<ProductReceiveGetDto> searchList() {
        List<ProductReceiveEntity> entities = productReceiveRepository.findAll();
        List<ProductReceiveGetDto> dtos = new ArrayList<>();

        for(ProductReceiveEntity entity : entities){
            dtos.add(ProductReceiveGetDto.toDto(entity));
        }
        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductOption cid 값과 상응되는 ProductReceive 데이터를 조회한다.
     *
     * @param productOptionCid : Integer
     * @return List::ProductReceiveGetDto
     * @see ProductReceiveRepository#findByProductOptionCid
     * @see ProductReceiveGetDto#toDto
     */
    public List<ProductReceiveGetDto> searchList(Integer productOptionCid) {
        List<ProductReceiveEntity> productEntities = productReceiveRepository.findByProductOptionCid(productOptionCid);
        List<ProductReceiveGetDto> dtos = new ArrayList<>();

        for(ProductReceiveEntity entity : productEntities){
            dtos.add(ProductReceiveGetDto.toDto(entity));
        }

        return dtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductReceive 데이터를 모두 조회한다.
     * 해당 ProductReceive와 연관관계에 놓여있는 Many To One JOIN(m2oj) 상태를 조회한다.
     *
     * @return List::ProductReceiveJoinResDto::
     * @see ProductReceiveRepository#selectAll
     */
    public List<ProductReceiveJoinResDto> searchListM2OJ() {
        List<ProductReceiveJoinResDto> productReceiveJoinResDtos = new ArrayList<>();
        List<ProductReceiveProj> productReceiveProjs = productReceiveRepository.selectAll();
        
        for (ProductReceiveProj projReceiveOpt : productReceiveProjs) {
            productReceiveJoinResDtos.add(searchOneM2OJ(projReceiveOpt.getProductReceive().getCid()));
        }
        return productReceiveJoinResDtos;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductReceive 내용을 한개 등록한다.
     * 상품 입고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReceiveGetDto : ProductReceiveGetDto
     * @param userId : UUID
     * @see ProductReceiveEntity#toEntity
     * @see ProductReceiveRepository#save
     */
    public ProductReceiveEntity createPR(ProductReceiveGetDto productReceiveGetDto, UUID userId) {
        productReceiveGetDto.setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId);

        ProductReceiveEntity entity = ProductReceiveEntity.toEntity(productReceiveGetDto);
        productReceiveRepository.save(entity);

        return entity;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ProductReceive 내용을 여러개 등록한다.
     * 상품 입고 개수만큼 ProductOption 데이터에 반영한다.
     * 
     * @param productReceiveGetDto : List::ProductReceiveGetDto::
     * @param userId : UUID
     * @see ProductReceiveEntity#toEntity
     * @see ProductOptionService#receiveProductUnit
     * @see ProductReceiveRepository#saveAll
     */
    public List<ProductReceiveEntity> createPRList(List<ProductReceiveGetDto> productReceiveGetDtos, UUID userId) {
        List<ProductReceiveEntity> entities = new ArrayList<>();
        
        for(ProductReceiveGetDto dto : productReceiveGetDtos) {
            dto.setCreatedAt(dateHandler.getCurrentDate()).setCreatedBy(userId);
            
            ProductReceiveEntity entity = ProductReceiveEntity.toEntity(dto);
            entities.add(entity);
        }

        productReceiveRepository.saveAll(entities);
        return entities;
    }

    

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터를 삭제한다.
     * 
     * @param productReceiveCid : Integer
     * @param userId : UUID
     * @see ProductReceiveRepository#findById
     * @see ProductOptionService#releaseProductUnit
     * @see ProductReceiveRepository#delete
     */
    public void destroyOne(Integer productReceiveCid, UUID userId) {
        productReceiveRepository.findById(productReceiveCid).ifPresent(product -> {
            productOptionService.releaseProductUnit(product.getProductOptionCid(), userId, product.getReceiveUnit());
            productReceiveRepository.delete(product);
        });
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param receiveDto : ProductReceiveGetDto
     * @param userId : UUID
     * @see ProductReceiveRepository#findById
     * @see ProductOptionService#releaseProductUnit
     * @see ProductReceiveRepository#save
     * @see ProductOptionService#receiveProductUnit
     */
    public void changeOne(ProductReceiveGetDto receiveDto, UUID userId) {
        productReceiveRepository.findById(receiveDto.getCid()).ifPresentOrElse(changeEntity -> {
            
            // 현재 저장된 입고 데이터 삭제
            productOptionService.releaseProductUnit(changeEntity.getProductOptionCid(), userId, changeEntity.getReceiveUnit());

            // 변경된 입고 데이터 저장
            changeEntity.setCid(receiveDto.getCid())
                        .setReceiveUnit(receiveDto.getReceiveUnit())
                        .setMemo(receiveDto.getMemo())
                        .setProductOptionCid(receiveDto.getProductOptionCid());
            
            productReceiveRepository.save(changeEntity);
            productOptionService.receiveProductUnit(changeEntity.getProductOptionCid(), userId, changeEntity.getReceiveUnit());
        }, null);
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 각 상품마다 ProductReceive cid 값과 상응되는 데이터를 업데이트한다.
     * 
     * @param productReceiveGetDtos : List::ProductReceiveGetDto::
     * @param userId :: UUID
     */
    public void changeList(List<ProductReceiveGetDto> productReceiveGetDtos, UUID userId) {

        for(ProductReceiveGetDto dto : productReceiveGetDtos) {
            changeOne(dto, userId);
        }
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * ProductReceive cid 값과 상응되는 데이터의 일부분을 업데이트한다.
     * 
     * @param receiveDto : ProductReceiveGetDto
     * @param userId : UUID
     * @see ProductReceiveRepository#findById
     * @see ProductOptionService#releaseProductUnit
     * @see ProductReceiveRepository#save
     * @see ProductOptionService#receiveProductUnit
     */
    public void patchOne(ProductReceiveGetDto receiveDto, UUID userId) {
        productReceiveRepository.findById(receiveDto.getCid()).ifPresentOrElse(receiveEntity -> {
            if(receiveDto.getReceiveUnit() != null){
               
                // 현재 저장된 입고 상품 개수 삭제
                productOptionService.releaseProductUnit(receiveEntity.getProductOptionCid(), userId, receiveEntity.getReceiveUnit());

                // 변경된 입고 상품 개수 저장
                receiveEntity.setReceiveUnit(receiveDto.getReceiveUnit());
                productReceiveRepository.save(receiveEntity);
                productOptionService.receiveProductUnit(receiveEntity.getProductOptionCid(), userId, receiveEntity.getReceiveUnit());
            }
            if(receiveDto.getMemo() != null) {
                receiveEntity.setMemo(receiveDto.getMemo());
            }
            if(receiveDto.getProductOptionCid() != null) {
                // 현재 저장된 입고 상품 개수 삭제
                productOptionService.releaseProductUnit(receiveEntity.getProductOptionCid(), userId, receiveEntity.getReceiveUnit());

                // 변경된 입고 상품 개수 및 데이터 저장
                receiveEntity.setProductOptionCid(receiveDto.getProductOptionCid());
                productReceiveRepository.save(receiveEntity);
                productOptionService.receiveProductUnit(receiveEntity.getProductOptionCid(), userId, receiveEntity.getReceiveUnit());
            }
        }, null);
    }
}
