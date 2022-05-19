package com.piaar_store_manager.server.service.shipment.packing_list;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import com.piaar_store_manager.server.model.shipment.dto.PackingListCommonGetDto;
import com.piaar_store_manager.server.model.shipment.dto.PackingListNaverExcelFormDto;
import com.piaar_store_manager.server.model.shipment.dto.PackingListOptionGetDto;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

@Service
public class PackingListNaverService {
    public List<PackingListCommonGetDto> getPackingListData(Sheet worksheet){
        
        List<PackingListNaverExcelFormDto> excelFormDtos = readExcelForm(worksheet); // Master Data

        List<PackingListCommonGetDto> packingListCommonGetDtos = new ArrayList<>();
        Set<String> packingListCommonSet = new TreeSet<>();

        for(PackingListNaverExcelFormDto excelFormDto : excelFormDtos){
            if(packingListCommonSet.add(excelFormDto.getProdName())){
                PackingListCommonGetDto commonGetDto = new PackingListCommonGetDto(excelFormDto.getProdName());
                packingListCommonGetDtos.add(commonGetDto);
            }
        }

        for(PackingListCommonGetDto commonGetDto : packingListCommonGetDtos){
            commonGetDto.setOptionInfos(
                getPackingListOptionGetDtos(commonGetDto, excelFormDtos)
            );
            commonGetDto.setUnitSum(
                getProdUnitSum(commonGetDto.getOptionInfos())
            );
        }


        return packingListCommonGetDtos;
    }

    public List<PackingListOptionGetDto> getPackingListOptionGetDtos(PackingListCommonGetDto commonGetDto, List<PackingListNaverExcelFormDto> excelFormDtos){
        List<PackingListOptionGetDto> optionGetDtos = new ArrayList<>();
        Set<String> optionSet = new TreeSet<>();
        
        for(PackingListNaverExcelFormDto excelFormDto : excelFormDtos){
            if(commonGetDto.getProdName().equals(excelFormDto.getProdName())){
                if(optionSet.add(excelFormDto.getOptionInfo())){
                    PackingListOptionGetDto optionGetDto = new PackingListOptionGetDto(excelFormDto.getProdName(), excelFormDto.getOptionInfo());
                    optionGetDtos.add(optionGetDto);
                }
            }
        }

        for(PackingListOptionGetDto optionGetDto : optionGetDtos){
            for(PackingListNaverExcelFormDto excelFormDto : excelFormDtos){
                if(optionGetDto.getFullName().equals(excelFormDto.getProdName()+"::"+excelFormDto.getOptionInfo())){
                    Integer unitSum = optionGetDto.getUnitSum() + excelFormDto.getUnit();
                    optionGetDto.setUnitSum(unitSum);
                }
            }
        }

        return optionGetDtos;
    }

    public Integer getProdUnitSum(List<PackingListOptionGetDto> optionGetDtos){
        Integer unitSum = 0;
        for(PackingListOptionGetDto dto : optionGetDtos){
            unitSum += dto.getUnitSum();
        }
        return unitSum;
    }

    public List<PackingListNaverExcelFormDto> readExcelForm(Sheet worksheet){
        List<PackingListNaverExcelFormDto> dataList = new ArrayList<>();
        for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4

            Row row = worksheet.getRow(i);

            PackingListNaverExcelFormDto data = new PackingListNaverExcelFormDto();
            data.setProdOrderNo(row.getCell(0).getStringCellValue());
            data.setOrderNo(row.getCell(1).getStringCellValue());
            data.setBuyer(row.getCell(8).getStringCellValue());
            data.setReceiver(row.getCell(10).getStringCellValue());
            data.setPaidTime(row.getCell(14).getDateCellValue());
            data.setProdNo(row.getCell(15).getStringCellValue());
            data.setProdName(row.getCell(16).getStringCellValue());
            data.setOptionInfo(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "");
            data.setUnit((int) row.getCell(20).getNumericCellValue());
            data.setProdCode(row.getCell(37) != null ? row.getCell(37).getStringCellValue() : "");
            data.setReceiverContact1(row.getCell(40).getStringCellValue());
            data.setReceiverContact2(row.getCell(41) != null ? row.getCell(41).getStringCellValue() : "");
            data.setDestination(row.getCell(42).getStringCellValue());
            data.setBuyerContact(row.getCell(43).getStringCellValue());
            data.setZipcode(row.getCell(44).getStringCellValue());
            data.setDeliveryMessage(row.getCell(45) != null ? row.getCell(45).getStringCellValue() : "");
            data.setProdName(row.getCell(16).getStringCellValue());
            dataList.add(data);
        }
        return dataList;
    }
}
