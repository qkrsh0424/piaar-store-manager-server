package com.piaar_store_manager.server.domain.sales_analysis.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.sales_analysis.dto.SalesAnalysisItemDto;
import com.piaar_store_manager.server.domain.sales_analysis.proj.SalesAnalysisItemProj;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesAnalysisItemBusinessService {
    private final ProductOptionService productOptionService;

    /**
     * <b></b>
     * <p>
     * startDate, endDate 날짜 사이의 판매랭킹 데이터를 모두 조회한다.
     * 네이버, 쿠팡, 피아르 erp의 판매수량을 모두 더해 전체판매수량 값 세팅.
     * 
     * @param params : Map[startDate, endDate]
     * @return List::SalesAnalysisItemDto::
     */
    public List<SalesAnalysisItemDto> searchAll(Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = params.get("startDate") != null ? LocalDateTime.parse(params.get("startDate").toString(), formatter) : LocalDateTime.of(1970, 1, 1, 0, 0);  /* 지정된 startDate 값이 있다면 해당 데이터로 조회, 없다면 1970년을 기준으로 조회 */
        LocalDateTime endDate = params.get("endDate") != null ? LocalDateTime.parse(params.get("endDate").toString(), formatter) : LocalDateTime.now();   /* 지정된 endDate 값이 있다면 해당 데이터로 조회, 없다면 현재시간을 기준으로 조회 */

        List<SalesAnalysisItemProj> salesItemList = productOptionService.findSalesAnalysisItem(startDate, endDate);
        List<SalesAnalysisItemDto> salesItemDtos = salesItemList.stream().map(proj -> SalesAnalysisItemDto.toDto(proj)).collect(Collectors.toList());

        // salesItemDtos.stream().forEach(r -> r.setTotalSalesUnit(r.getNaverSalesUnit() + r.getCoupangSalesUnit() + r.getErpSalesUnit()));
        // salesItemDtos.sort(Comparator.comparing(SalesAnalysisItemDto::getTotalSalesUnit).reversed());

        return salesItemDtos;
    }
}
