package com.piaar_store_manager.server.domain.order_confirm.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.piaar_store_manager.server.domain.order_confirm.dto.OrderConfirmGetDto;
import com.piaar_store_manager.server.domain.order_confirm.dto.OrdererDto;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderConfirmService {
    public List<OrderConfirmGetDto> getReadExcel(Sheet worksheet){
        List<OrderConfirmGetDto> orderConfirmGetDtos = new ArrayList<>();
        

        Set<String> prodNameSet = new HashSet<>();
        for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            // log
            log.info("excel size number : ", i);
            log.info("OrderConfirmService : getReadExcel : row.getCell => {}." , row.getCell(16));
            String prodName = row.getCell(16).getStringCellValue() + "-" + (row.getCell(18) != null ? row.getCell(18).getStringCellValue():"");
            if(prodNameSet.add(prodName)){
                OrderConfirmGetDto orderConfirmGetDto = new OrderConfirmGetDto();
                orderConfirmGetDto.setUuid(UUID.randomUUID().toString());
                orderConfirmGetDto.setProdFullName(prodName);
                orderConfirmGetDto.setProdName(row.getCell(16).getStringCellValue());
                orderConfirmGetDto.setOptionInfo(row.getCell(18) != null ? row.getCell(18).getStringCellValue():"");
                orderConfirmGetDtos.add(orderConfirmGetDto);
            }
        }

        Collections.sort(orderConfirmGetDtos);

        for(int i = 0 ; i < orderConfirmGetDtos.size() ; i++){
            int unit = 0;
            for (int j = 2; j < worksheet.getPhysicalNumberOfRows(); j++) {
                Row row = worksheet.getRow(j);
                String prodName = row.getCell(16).getStringCellValue() + "-" + (row.getCell(18) != null ? row.getCell(18).getStringCellValue():"");
                if(prodName.equals((String) orderConfirmGetDtos.get(i).getProdFullName())){
                    unit = (int) (unit + row.getCell(20).getNumericCellValue());
                }
            }
            orderConfirmGetDtos.get(i).setUnit(unit);
        }
        for(int i = 0 ; i < orderConfirmGetDtos.size(); i++){
            List<OrdererDto> ordererList = new ArrayList<>();
            for(int j = 2; j < worksheet.getPhysicalNumberOfRows(); j++){
                Row row = worksheet.getRow(j);
                String prodName = row.getCell(16).getStringCellValue() + "-" + (row.getCell(18) != null ? row.getCell(18).getStringCellValue():"");
                if(orderConfirmGetDtos.get(i).getProdFullName().equals(prodName)){
                    OrdererDto ordererDto = new OrdererDto();
                    ordererDto.setName(row.getCell(8).getStringCellValue());
                    ordererDto.setReceiverName(row.getCell(10).getStringCellValue());
                    ordererDto.setAddress(row.getCell(42).getStringCellValue());
                    ordererDto.setPhone(row.getCell(43).getStringCellValue());
                    ordererDto.setOrderUnit((int) row.getCell(20).getNumericCellValue());
                    ordererDto.setOrderDate(row.getCell(14).getLocalDateTimeCellValue());
                    ordererDto.setDeliveryLimitDate(row.getCell(28).getLocalDateTimeCellValue());
                    ordererList.add(ordererDto);
                }
            }
            orderConfirmGetDtos.get(i).setOrdererList(ordererList);
        }
        return orderConfirmGetDtos;
    }
}
