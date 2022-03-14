package com.piaar_store_manager.server.domain.sales_analysis.service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    public List<SalesAnalysisItemDto> searchAll(Map<String, Object> params) {
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.set(Calendar.YEAR, 1970);
        Date startDate = params.get("startDate") != null ? new Date(params.get("startDate").toString())
                : startDateCalendar.getTime();

        Date endDate = params.get("endDate") != null ? new Date(params.get("endDate").toString()) : new Date();

        List<SalesAnalysisItemProj> salesItemList = itemRepository.findSalesAnalysisItem(startDate, endDate);
        List<SalesAnalysisItemDto> salesItemDtos = salesItemList.stream().map(proj -> SalesAnalysisItemDto.toDto(proj)).collect(Collectors.toList());
        return salesItemDtos;
    }
}
