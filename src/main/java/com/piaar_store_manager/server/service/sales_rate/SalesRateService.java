package com.piaar_store_manager.server.service.sales_rate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.model.sales_rate.dto.OrderSearchExcelFormDto;
import com.piaar_store_manager.server.model.sales_rate.dto.SalesRateCommonGetDto;
import com.piaar_store_manager.server.model.sales_rate.dto.SalesRateOptionGetDto;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

// TODO : Code Refactoring All
@Service
public class SalesRateService {
    public List<SalesRateCommonGetDto> getSalesRateCommon(Sheet worksheet) {
        List<OrderSearchExcelFormDto> orderSearchExcelFormDtos = getOrderSearchExcelFormDtos(worksheet);

        List<SalesRateCommonGetDto> salesRateCommonGetDtos = new ArrayList<>();
        Set<String> prodNameSet = new TreeSet<>();

        List<SalesRateOptionGetDto> salesRateOptionGetDtos = new ArrayList<>();
        Set<String> prodNameAndOptionSet = new TreeSet<>();

        for (OrderSearchExcelFormDto dto : orderSearchExcelFormDtos) {
            // 클레임 상태가 "취소완료" 이면 건너뛴다
            if (dto.getClaimStatus() != null && (dto.getClaimStatus().equals("취소완료") || dto.getClaimStatus().equals("반품완료"))) {
                continue;
            }
            if (prodNameSet.add(dto.getProdName())) {
                SalesRateCommonGetDto salesRateCommonGetDto = new SalesRateCommonGetDto(dto.getProdName());

                salesRateCommonGetDtos.add(salesRateCommonGetDto);
            }

            if (prodNameAndOptionSet.add(dto.getProdName() + dto.getOptionInfo())) {
                SalesRateOptionGetDto salesRateCommonGetDto = new SalesRateOptionGetDto(dto.getProdName(),
                        dto.getOptionInfo());

                salesRateOptionGetDtos.add(salesRateCommonGetDto);
            }
        }

        for (SalesRateOptionGetDto salesRateOptionGetDto : salesRateOptionGetDtos) {
            for (OrderSearchExcelFormDto dto : orderSearchExcelFormDtos) {
                // 클레임 상태가 "취소완료" 이면 건너뛴다
                if (dto.getClaimStatus() != null && (dto.getClaimStatus().equals("취소완료") || dto.getClaimStatus().equals("반품완료"))) {
                    continue;
                }

                if (salesRateOptionGetDto.getFullName().equals(dto.getProdName() + "::" + dto.getOptionInfo())) {
                    Integer unitCount = salesRateOptionGetDto.getUnitSum();
                    unitCount += dto.getUnit();

                    salesRateOptionGetDto.setUnitSum(unitCount);
                }
            }
        }

        for (SalesRateCommonGetDto salesRateCommonGetDto : salesRateCommonGetDtos) {
            salesRateOptionGetDtos.stream()
                    .filter(r -> r.getProdName().equals(salesRateCommonGetDto.getProdName()))
                    .forEach(r->{
                        salesRateCommonGetDto.setUnitSum(salesRateCommonGetDto.getUnitSum() + r.getUnitSum());
                        salesRateCommonGetDto.getOptionInfos().add(r);
                    });
        }

        return salesRateCommonGetDtos;
    }

    public List<OrderSearchExcelFormDto> getOrderSearchExcelFormDtos(Sheet worksheet) {
        List<OrderSearchExcelFormDto> dataList = new ArrayList<>();
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4

            Row row = worksheet.getRow(i);

            OrderSearchExcelFormDto data = new OrderSearchExcelFormDto();
            data.setProdOrderNo(row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "");
            data.setOrderNo(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "");
            data.setOrderTime(row.getCell(2) != null ? row.getCell(2).getDateCellValue() : null);
            data.setOrderStatus(row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "");
            data.setClaimStatus(row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "");
            data.setProdNo(row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "");
            data.setProdName(row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "");
            data.setOptionInfo(row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "");
            data.setUnit(row.getCell(8) != null ? (int) row.getCell(8).getNumericCellValue() : 0);
            data.setBuyerName(row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "");
            data.setBuyerId(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "");
            data.setReceiverName(row.getCell(11) != null ? row.getCell(11).getStringCellValue() : "");
            dataList.add(data);
        }
        return dataList;
    }
}
