package com.piaar_store_manager.server.service.product;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.product.dto.ProductCreateReqDto;
import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductBusinessService {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductOptionService productOptionService;

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product와 ProductOption 내용을 한개 등록한다.
     * 
     * @param productCreateReqDtos : List::ProductCreateReqDto::
     * @param userId : UUID
     * @see ProductService#createOne
     * @see productOptionService#createOne
     */
    public void createPAO(ProductCreateReqDto reqDto, UUID userId) {
        ProductEntity savedProductEntity = new ProductEntity();

        savedProductEntity = productService.createOne(reqDto.getProductDto(), userId);
            
        for (ProductOptionGetDto dto : reqDto.getOptionDtos()) {
            productOptionService.createOne(dto, userId, savedProductEntity.getCid());
        }
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product와 ProductOption 내용을 여러개 등록한다.
     * 
     * @param productCreateReqDtos : List::ProductCreateReqDto::
     * @param userId : UUID
     * @see ProductService#createOne
     * @see productOptionService#createOne
     */
    public void createPAOList(List<ProductCreateReqDto> productCreateReqDtos, UUID userId) {
        ProductEntity savedProductEntity = new ProductEntity();

        for (ProductCreateReqDto reqDto : productCreateReqDtos) {
            savedProductEntity = productService.createOne(reqDto.getProductDto(), userId);
            
            for (ProductOptionGetDto dto : reqDto.getOptionDtos()) {
                productOptionService.createOne(dto, userId, savedProductEntity.getCid());
            }
        }
    }
}
