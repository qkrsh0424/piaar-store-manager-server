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
import com.piaar_store_manager.server.service.product.ProductService;
import com.piaar_store_manager.server.service.product_category.ProductCategoryService;
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
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private UserService userService;

    public ProductReceiveGetDto searchOne(Integer productReceiveId) {
        Optional<ProductReceiveEntity> productEntityOpt = productReceiveRepository.findById(productReceiveId);
        ProductReceiveGetDto dto = new ProductReceiveGetDto();

        if (productEntityOpt.isPresent()) {
            dto = getDtoByEntity(productEntityOpt.get());
        } else {
            throw new NullPointerException();
        }

        return dto;
    }

    public ProductReceiveGetDto getDtoByEntity(ProductReceiveEntity entity) {
        ProductReceiveGetDto dto = new ProductReceiveGetDto();

        dto.setCid(entity.getCid())
           .setId(entity.getId())
           .setReceiveUnit(entity.getReceiveUnit())
           .setMemo(entity.getMemo())
           .setCreatedAt(entity.getCreatedAt())
           .setCreatedBy(entity.getCreatedBy())
           .setProductOptionCid(entity.getProductOptionCid());

        return dto;
    }

    public List<ProductReceiveGetDto> searchList() {
        List<ProductReceiveEntity> entities = productReceiveRepository.findAll();
        List<ProductReceiveGetDto> dtos = new ArrayList<>();

        for(ProductReceiveEntity entity : entities){
            dtos.add(getDtoByEntity(entity));
        }
        return dtos;
    }

    public void createRP(ProductReceiveGetDto productReceiveGetDto, UUID userId) {
        ProductReceiveEntity entity = convEntityByDto(productReceiveGetDto, userId);
        productReceiveRepository.save(entity);
        productOptionService.receiveProductUnit(productReceiveGetDto.getProductOptionCid(), userId, entity.getReceiveUnit());
    }

    public ProductReceiveEntity convEntityByDto(ProductReceiveGetDto dto, UUID userId) {
        ProductReceiveEntity entity = new ProductReceiveEntity();

        entity.setId(UUID.randomUUID())
              .setReceiveUnit(dto.getReceiveUnit())
              .setMemo(dto.getMemo())
              .setCreatedAt(dateHandler.getCurrentDate())
              .setCreatedBy(userId)
              .setProductOptionCid(dto.getProductOptionCid());

        return entity;
    }

    public void createRPList(List<ProductReceiveGetDto> productReceiveGetDtos, UUID userId) {
        ProductReceiveEntity entity = new ProductReceiveEntity();
        
        for(ProductReceiveGetDto dto : productReceiveGetDtos) {
            entity = convEntityByDto(dto, userId);
            productReceiveRepository.save(entity);
            productOptionService.receiveProductUnit(dto.getProductOptionCid(), userId, entity.getReceiveUnit());
        }
    }

    public void destroyOne(Integer productReceiveId, UUID userId) {
        productReceiveRepository.findById(productReceiveId).ifPresent(releaseProduct -> {
            productOptionService.releaseProductUnit(releaseProduct.getProductOptionCid(), userId, releaseProduct.getReceiveUnit());
            productReceiveRepository.delete(releaseProduct);
        });
    }

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

    public void changeList(List<ProductReceiveGetDto> productReceiveGetDtos, UUID userId) {

        for(ProductReceiveGetDto dto : productReceiveGetDtos) {
            changeOne(dto, userId);
        }
    }

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

    public ProductReceiveJoinResDto searchOneOTMJ(Integer productReceiveId){
        ProductReceiveJoinResDto productReceiveResDto = new ProductReceiveJoinResDto();

        Optional<ProductReceiveProj> productReceiveProjOpt = productReceiveRepository.selectByCid(productReceiveId);

        if(productReceiveProjOpt.isPresent()) {
            ProductReceiveGetDto productReceiveDto = this.getDtoByEntity(productReceiveProjOpt.get().getProductReceive());
            ProductOptionGetDto productOptionDto = productOptionService.getDtoByEntity(productReceiveProjOpt.get().getProductOption());
            ProductGetDto productDto = productService.getDtoByEntity(productReceiveProjOpt.get().getProduct());
            ProductCategoryGetDto productCategoryDto = productCategoryService.getDtoByEntity(productReceiveProjOpt.get().getCategory());
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

    public List<ProductReceiveJoinResDto> searchListOTMJ() {
        List<ProductReceiveJoinResDto> productReceiveJoinResDtos = new ArrayList<>();
        List<ProductReceiveProj> productReceiveProjs = productReceiveRepository.selectAll();
        
        for (ProductReceiveProj projReceiveOpt : productReceiveProjs) {
            productReceiveJoinResDtos.add(searchOneOTMJ(projReceiveOpt.getProductReceive().getCid()));
        }
        return productReceiveJoinResDtos;
    }

}
