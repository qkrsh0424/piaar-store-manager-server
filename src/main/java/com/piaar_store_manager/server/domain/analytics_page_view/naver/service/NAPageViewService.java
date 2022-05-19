package com.piaar_store_manager.server.domain.analytics_page_view.naver.service;

import java.util.ArrayList;
import java.util.List;

import com.piaar_store_manager.server.domain.analytics_page_view.naver.dto.NAPopularPageExcelFormDto;
import com.piaar_store_manager.server.domain.analytics_page_view.naver.dto.NAProductPageViewDto;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

@Service
public class NAPageViewService {
    public List<NAProductPageViewDto> getProductPageViews(Sheet worksheet) {
        List<NAProductPageViewDto> productPageViewDtos = new ArrayList<>();
        List<NAPopularPageExcelFormDto> excelFormDtos = readNAPopularPageExcelForm(worksheet);

        for (NAPopularPageExcelFormDto excelFormDto : excelFormDtos) {
            if (excelFormDto.getPageUrl().contains("/products/")) {
                NAProductPageViewDto productPageViewDto = new NAProductPageViewDto();
                productPageViewDto.setProdNo(null);
                productPageViewDto.setDeviceType("");
                productPageViewDto.setPageUrl(excelFormDto.getPageUrl());
                productPageViewDto.setPageView(excelFormDto.getPageView());
                productPageViewDto.setAvgResidenceTime(excelFormDto.getAvgResidenceTime());
                productPageViewDto.setRatio(excelFormDto.getRatio());

                productPageViewDtos.add(productPageViewDto);
            }

        }

        for (NAProductPageViewDto dto : productPageViewDtos) {
            String pageUrl = dto.getPageUrl();
            if (pageUrl.split("://")[1] != null && pageUrl.split("://")[1].startsWith("m.")) {
                dto.setDeviceType("mobile");
            } else {
                dto.setDeviceType("desktop");
            }

            if(pageUrl.split("/products/")[1] != null){
                dto.setProdNo(pageUrl.split("/products/")[1]);
            }else{
                dto.setProdNo("");
            }
        }
        return productPageViewDtos;
    }

    public List<NAPopularPageExcelFormDto> readNAPopularPageExcelForm(Sheet worksheet) {
        List<NAPopularPageExcelFormDto> dataList = new ArrayList<>();
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4

            Row row = worksheet.getRow(i);

            NAPopularPageExcelFormDto data = new NAPopularPageExcelFormDto();

            data.setPageUrl(row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "");
            data.setPageView(row.getCell(1) != null ? (int) row.getCell(1).getNumericCellValue() : 0);
            data.setAvgResidenceTime(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "");
            data.setRatio(row.getCell(3) != null ? String.valueOf(row.getCell(3).getNumericCellValue()) : "");
            dataList.add(data);
        }
        return dataList;
    }
}
