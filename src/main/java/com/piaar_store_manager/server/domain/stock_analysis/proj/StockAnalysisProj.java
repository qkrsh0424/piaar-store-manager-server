package com.piaar_store_manager.server.domain.stock_analysis.proj;

import java.time.LocalDateTime;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StockAnalysisProj {
    ProductEntity product;
    ProductOptionEntity option;
    ProductCategoryEntity category;
    Integer releasedUnit;
    Integer receivedUnit;
    LocalDateTime lastReleasedAt;
}
