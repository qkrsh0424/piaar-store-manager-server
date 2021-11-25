package com.piaar_store_manager.server.service.product_category;

import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryBusinessService {
    private ProductCategoryService productCategoryService;

    @Autowired
    public ProductCategoryBusinessService(
        ProductCategoryService productCategoryService
    ) {
        this.productCategoryService = productCategoryService;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductCategory 데이터를 모두 조회한다.
     * 조회된 데이터를 dto로 변환한다.
     *
     * @return List::ProductCategoryGetDto::
     * @see ProductCategoryService:searchList
     * @see ProductCategoryGetDto:toDto
     */
    public List<ProductCategoryGetDto> searchList(){
        List<ProductCategoryEntity> productCategoryEntities = productCategoryService.searchList();
        List<ProductCategoryGetDto> productCategoryGetDtos = productCategoryEntities.stream().map(r -> ProductCategoryGetDto.toDto(r)).collect(Collectors.toList());
        return productCategoryGetDtos;
    }
}
