package com.piaar_store_manager.server.service.product_category;

import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryJEntity;
import com.piaar_store_manager.server.model.product_category.repository.ProductCategoryJRepository;
import com.piaar_store_manager.server.model.product_category.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<ProductCategoryGetDto> productCategoryGetDtos = new ArrayList<>();

        for(ProductCategoryEntity productCategoryEntity : productCategoryEntities){
            productCategoryGetDtos.add(getDtoByEntity(productCategoryEntity));
        }
        return productCategoryGetDtos;
    }

    public ProductCategoryGetDto getDtoByEntity(ProductCategoryEntity productCategoryEntity){

            ProductCategoryGetDto productCategoryDto = new ProductCategoryGetDto();

            productCategoryDto
                .setCid(productCategoryEntity.getCid())
                .setId(productCategoryEntity.getId())
                .setName(productCategoryEntity.getName());

        return productCategoryDto;
    }

    // test
    @Autowired
    private ProductCategoryJRepository productCategoryJRepository;

    public ProductCategoryJEntity searchJoinOne(Integer productId){
        Optional<ProductCategoryJEntity> productCategoryEntities = productCategoryJRepository.findById(productId);
        
        return productCategoryEntities.get();
    }
}
