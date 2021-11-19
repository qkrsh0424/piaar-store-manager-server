package com.piaar_store_manager.server.service.product_category;

import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.model.product_category.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ProductCategory 데이터를 모두 조회한다.
     *
     * @return List::ProductCategoryEntity::
     * @see ProductCategoryRepository#findAll
     */
    public List<ProductCategoryEntity> searchList(){
        return productCategoryRepository.findAll();
    }
}
