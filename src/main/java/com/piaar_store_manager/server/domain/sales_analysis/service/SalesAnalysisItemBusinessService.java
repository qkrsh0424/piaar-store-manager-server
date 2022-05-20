package com.piaar_store_manager.server.domain.sales_analysis.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.delivery_ready.naver.repository.DeliveryReadyNaverItemRepository;
import com.piaar_store_manager.server.domain.sales_analysis.dto.SalesAnalysisItemDto;
import com.piaar_store_manager.server.domain.sales_analysis.proj.SalesAnalysisItemProj;
import com.piaar_store_manager.server.domain.user.service.UserService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesAnalysisItemBusinessService {
    private final DeliveryReadyNaverItemRepository itemRepository;
    private final UserService userService;

    public List<SalesAnalysisItemDto> searchAll(Map<String, Object> params) {
        // access check
        userService.userLoginCheck();
        userService.userManagerRoleCheck();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = params.get("startDate") != null ? LocalDateTime.parse(params.get("startDate").toString(), formatter) : LocalDateTime.of(1970, 1, 1, 0, 0);  /* 지정된 startDate 값이 있다면 해당 데이터로 조회, 없다면 1970년을 기준으로 조회 */
        LocalDateTime endDate = params.get("endDate") != null ? LocalDateTime.parse(params.get("endDate").toString(), formatter) : LocalDateTime.now();   /* 지정된 endDate 값이 있다면 해당 데이터로 조회, 없다면 현재시간을 기준으로 조회 */

        List<SalesAnalysisItemProj> salesItemList = itemRepository.findSalesAnalysisItem(startDate, endDate);
        List<SalesAnalysisItemDto> salesItemDtos = salesItemList.stream().map(proj -> SalesAnalysisItemDto.toDto(proj)).collect(Collectors.toList());

        salesItemDtos.stream().forEach(r -> r.setTotalSalesUnit(r.getNaverSalesUnit() + r.getCoupangSalesUnit() + r.getErpSalesUnit()));
        salesItemDtos.sort(Comparator.comparing(SalesAnalysisItemDto::getTotalSalesUnit).reversed());

        return salesItemDtos;
    }
}
