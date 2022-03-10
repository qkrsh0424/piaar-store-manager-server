package com.piaar_store_manager.server.domain.sales_analysis.service;

import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.sales_analysis.dto.SalesAnalysisItemDto;
import com.piaar_store_manager.server.domain.sales_analysis.proj.SalesAnalysisItemProj;
import com.piaar_store_manager.server.model.delivery_ready.naver.repository.DeliveryReadyNaverItemRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesAnalysisItemBusinessService {
    private final DeliveryReadyNaverItemRepository itemRepository;

    public List<SalesAnalysisItemDto> searchAll() {
        List<SalesAnalysisItemProj> salesItemList = itemRepository.findSalesAnalysisItem();
        List<SalesAnalysisItemDto> salesItemDtos = salesItemList.stream().map(proj -> SalesAnalysisItemDto.toDto(proj)).collect(Collectors.toList());
        return salesItemDtos;
    }
}
