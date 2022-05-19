package com.piaar_store_manager.server.service.shipment.packing_list;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.piaar_store_manager.server.model.shipment.dto.PackingListCommonGetDto;
import com.piaar_store_manager.server.model.shipment.dto.PackingListCoupangExcelFormDto;
import com.piaar_store_manager.server.model.shipment.dto.PackingListOptionGetDto;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

@Service
public class PackingListCoupangService {
    public List<PackingListCommonGetDto> getPackingListData(Sheet worksheet){
        
        List<PackingListCoupangExcelFormDto> excelFormDtos = readExcelForm(worksheet); // Master Data

        List<PackingListCommonGetDto> packingListCommonGetDtos = new ArrayList<>();
        Set<String> packingListCommonSet = new TreeSet<>();

        for(PackingListCoupangExcelFormDto excelFormDto : excelFormDtos){
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

    public List<PackingListOptionGetDto> getPackingListOptionGetDtos(PackingListCommonGetDto commonGetDto, List<PackingListCoupangExcelFormDto> excelFormDtos){
        List<PackingListOptionGetDto> optionGetDtos = new ArrayList<>();
        Set<String> optionSet = new TreeSet<>();
        
        for(PackingListCoupangExcelFormDto excelFormDto : excelFormDtos){
            if(commonGetDto.getProdName().equals(excelFormDto.getProdName())){
                if(optionSet.add(excelFormDto.getOptionInfo())){
                    PackingListOptionGetDto optionGetDto = new PackingListOptionGetDto(excelFormDto.getProdName(), excelFormDto.getOptionInfo());
                    optionGetDtos.add(optionGetDto);
                }
            }
        }

        for(PackingListOptionGetDto optionGetDto : optionGetDtos){
            for(PackingListCoupangExcelFormDto excelFormDto : excelFormDtos){
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

    public List<PackingListCoupangExcelFormDto> readExcelForm(Sheet worksheet){
        List<PackingListCoupangExcelFormDto> dataList = new ArrayList<>();
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4

            Row row = worksheet.getRow(i);

            PackingListCoupangExcelFormDto data = new PackingListCoupangExcelFormDto();
            data.setOrderNo(row.getCell(2).getStringCellValue());
            data.setBuyer(row.getCell(23).getStringCellValue());
            data.setReceiver(row.getCell(26).getStringCellValue());
            data.setPaidTime(row.getCell(9).getStringCellValue());
            data.setProdNo(row.getCell(12).getStringCellValue());
            data.setProdName(row.getCell(10).getStringCellValue());
            data.setOptionInfo(row.getCell(11) != null ? row.getCell(11).getStringCellValue() : "");
            data.setUnit(Integer.parseInt(row.getCell(22).getStringCellValue()));
            data.setProdCode(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "");
            data.setReceiverContact1(row.getCell(27).getStringCellValue());
            data.setDestination(row.getCell(29).getStringCellValue());
            data.setBuyerContact(row.getCell(25).getStringCellValue());
            data.setZipcode(row.getCell(28).getStringCellValue());
            data.setDeliveryMessage(row.getCell(30) != null ? row.getCell(30).getStringCellValue() : "");
            data.setProdName(row.getCell(10).getStringCellValue());
            dataList.add(data);
        }
        return dataList;
    }
}
