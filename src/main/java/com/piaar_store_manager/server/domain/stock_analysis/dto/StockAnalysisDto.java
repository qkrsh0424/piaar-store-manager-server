package com.piaar_store_manager.server.domain.stock_analysis.dto;

import java.time.LocalDateTime;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter @Setter
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAnalysisDto {
    private ProductOptionGetDto option;
    private ProductGetDto product;
    private ProductCategoryGetDto category;
    private Integer stockSumUnit;
    private LocalDateTime lastReleasedAt;
}
