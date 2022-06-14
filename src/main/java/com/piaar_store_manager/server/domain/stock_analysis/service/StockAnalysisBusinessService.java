package com.piaar_store_manager.server.domain.stock_analysis.service;

import java.util.List; 
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.stock_analysis.dto.StockAnalysisDto;
import com.piaar_store_manager.server.domain.stock_analysis.proj.StockAnalysisProj;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockAnalysisBusinessService {
    private final ProductOptionService productOptionService;

    public List<StockAnalysisDto> searchList() {
        List<StockAnalysisProj> projs = productOptionService.qfindStockAnalysis();

        List<StockAnalysisDto> dtos = projs.stream().map(r -> {
            Integer receivedUnit = r.getReceivedUnit() != null ? r.getReceivedUnit() : 0;
            Integer releasedUnit = r.getReleasedUnit() != null ? r.getReleasedUnit() : 0;
            Integer sumStockUnit = receivedUnit - releasedUnit;

            StockAnalysisDto dto = StockAnalysisDto.builder()
                .option(ProductOptionGetDto.toDto(r.getOption()))
                .product(ProductGetDto.toDto(r.getProduct()))
                .category(ProductCategoryGetDto.toDto(r.getCategory()))
                .stockSumUnit(sumStockUnit)
                .lastReleasedAt(r.getLastReleasedAt())
                .build();

            return dto;
        }).collect(Collectors.toList());
        
        return dtos;
    }
}
