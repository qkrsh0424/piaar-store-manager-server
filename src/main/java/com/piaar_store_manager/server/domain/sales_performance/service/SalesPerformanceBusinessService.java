package com.piaar_store_manager.server.domain.sales_performance.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_category.service.ProductCategoryService;
import com.piaar_store_manager.server.domain.sales_performance.dto.BestProductPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesCategoryPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesChannelPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesProductPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.filter.DashboardPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.SalesPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.proj.BestProductPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesProductPerformanceProjection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesPerformanceBusinessService {
    private final SalesPerformanceService salesPerformanceService;
    private final ProductCategoryService productCategoryService;

    public List<SalesPerformanceDto> searchDashboard(DashboardPerformanceSearchFilter filter) {
        List<SalesPerformanceProjection> projs = salesPerformanceService.qSearchDashBoardByParams(filter);
        List<SalesPerformanceDto> dtos = projs.stream().map(SalesPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesPerformanceDto> searchSalesPerformance(SalesPerformanceSearchFilter filter) {
        List<SalesPerformanceProjection> projs = salesPerformanceService.qSearchSalesPerformanceByParams(filter);
        List<SalesPerformanceDto> dtos = projs.stream().map(SalesPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesChannelPerformanceDto.Performance> searchSalesPerformanceByChannel(SalesPerformanceSearchFilter filter) {
        List<SalesChannelPerformanceProjection.Performance> projs = salesPerformanceService.qSearchChannelSalesPerformanceByFilter(filter);
        List<SalesChannelPerformanceDto.Performance> dtos = projs.stream().map(SalesChannelPerformanceDto.Performance::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesChannelPerformanceDto> searchProductSalesPerformanceByChannel(SalesPerformanceSearchFilter filter) {
        List<SalesChannelPerformanceProjection> projs = salesPerformanceService.qSearchProductChannelSalesPerformanceByFilter(filter);
        List<SalesChannelPerformanceDto> dtos = projs.stream().map(SalesChannelPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesCategoryPerformanceDto.Performance> searchSalesPerformanceByCategory(SalesPerformanceSearchFilter filter) {
        List<ProductCategoryEntity> categoryEntities = productCategoryService.searchAll();
        List<String> categoryName = categoryEntities.stream().map(r -> r.getName()).collect(Collectors.toList());
        List<SalesCategoryPerformanceProjection.Performance> projs = salesPerformanceService.qSearchCategorySalesPerformanceByParams(filter, categoryName);
        List<SalesCategoryPerformanceDto.Performance> dtos = projs.stream().map(SalesCategoryPerformanceDto.Performance::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesCategoryPerformanceDto.ProductPerformance> searchSalesProductPerformanceByCategory(SalesPerformanceSearchFilter filter) {
        List<ProductCategoryEntity> categoryEntities = productCategoryService.searchAll();
        List<String> categoryName = categoryEntities.stream().map(r -> r.getName()).collect(Collectors.toList());
        List<SalesCategoryPerformanceProjection.ProductPerformance> projs = salesPerformanceService.qSearchCategoryAndProductSalesPerformanceByParams(filter, categoryName);
        List<SalesCategoryPerformanceDto.ProductPerformance> dtos = projs.stream().map(SalesCategoryPerformanceDto.ProductPerformance::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesProductPerformanceDto.Performance> searchSalesPerformanceByProductOption(SalesPerformanceSearchFilter filter) {
        List<SalesProductPerformanceProjection.Performance> projs = salesPerformanceService.qSearchProductOptionSalesPerformanceByFilter(filter);
        List<SalesProductPerformanceDto.Performance> dtos = projs.stream().map(SalesProductPerformanceDto.Performance::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesPerformanceDto> searchSalesPerformanceByProduct(SalesPerformanceSearchFilter filter) {
        List<SalesPerformanceProjection> projs = salesPerformanceService.qSearchProductSalesPerformanceByFilter(filter);
        List<SalesPerformanceDto> dtos = projs.stream().map(SalesPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public Page<BestProductPerformanceDto> searchBestProductPerformance(SalesPerformanceSearchFilter filter, Pageable pageable) {
        Page<BestProductPerformanceProjection> projs = salesPerformanceService.qSearchBestProductPerformanceByFilter(filter, pageable);
        List<BestProductPerformanceDto> dtos = projs.stream().map(BestProductPerformanceDto::toDto).collect(Collectors.toList());
        return new PageImpl(dtos, pageable, projs.getTotalElements());
    }

    public List<BestProductPerformanceDto.RelatedProductOptionPerformance> searchProductOptionPerformanceByProduct(SalesPerformanceSearchFilter filter) {
        List<BestProductPerformanceProjection.RelatedProductOptionPerformance> projs = salesPerformanceService.qSearchProductOptionPerformanceByFilter(filter);
        List<BestProductPerformanceDto.RelatedProductOptionPerformance> dtos = projs.stream().map(BestProductPerformanceDto.RelatedProductOptionPerformance::toDto).collect(Collectors.toList());
        return dtos;
    }
}
