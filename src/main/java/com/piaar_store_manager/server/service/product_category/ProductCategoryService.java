package com.piaar_store_manager.server.service.product_category;

import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.model.product_category.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 상품카테고리를 모두 조회한다.
     *
     * @return List::ProductCategoryGetDto::
     * @see ProductCategoryRepository
     */
    public List<ProductCategoryGetDto> searchList(){
        List<ProductCategoryEntity> productCategoryEntities = productCategoryRepository.findAll();
        List<ProductCategoryGetDto> productCategoryGetDtos = getDtoByEntites(productCategoryEntities);

        return productCategoryGetDtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * List::ProductCategoryEntity:: => List::ProductCategoryGetDto::
     * @param productCategoryEntities
     * @return List::ProductCategoryGetDto::
     */
    private List<ProductCategoryGetDto> getDtoByEntites(List<ProductCategoryEntity> productCategoryEntities){
        List<ProductCategoryGetDto> productCategoryDtos = new ArrayList<>();

        for(ProductCategoryEntity productCategoryEntity : productCategoryEntities){
            ProductCategoryGetDto productCategoryDto = new ProductCategoryGetDto();

            productCategoryDto.setCid(productCategoryEntity.getCid())
                    .setName(productCategoryEntity.getName());

            productCategoryDtos.add(productCategoryDto);
        }
        return productCategoryDtos;
    }

}
