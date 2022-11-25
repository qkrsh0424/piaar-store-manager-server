package com.piaar_store_manager.server.domain.product_option.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionStockCycleDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductOptionBusinessService {
    private final ProductOptionService productOptionService;
    
    public List<ProductOptionGetDto> searchList() {
        List<ProductOptionEntity> entities = productOptionService.searchList();
        List<ProductOptionGetDto> dtos = entities.stream().map(entity -> ProductOptionGetDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    /*
     * 옵션에 등록된 출고지를 중복을 제거해 추출한다
     */
    public List<String> searchReleaseLocation() {
        List<ProductOptionEntity> entities = productOptionService.searchList();
        Set<String> releaseLocationSet = new HashSet<>();
        entities.forEach(r -> {
            if(r.getReleaseLocation() != null && !r.getReleaseLocation().equals("")){
                releaseLocationSet.add(r.getReleaseLocation());
            }
        });

        List<String> allReleaseLocation = releaseLocationSet.stream().map(r -> r).collect(Collectors.toList());
        return allReleaseLocation;
    }

    public List<ProductOptionStockCycleDto> searchStockCycle(Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String date = params.get("searchEndDate") != null ? params.get("searchEndDate").toString() : null;
        LocalDateTime searchEndDate = date != null ? LocalDateTime.parse(date, formatter) : LocalDateTime.now();

        Integer categoryCid = params.get("categoryCid") != null ? Integer.parseInt(params.get("categoryCid").toString()) : null;

        List<ProductOptionStockCycleDto> stockCycle = productOptionService.jdSearchStockStatusByWeek(searchEndDate, categoryCid);
        return stockCycle;
    }
}
