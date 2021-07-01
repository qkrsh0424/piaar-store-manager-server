package com.piaar_store_manager.server.service.waybill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import com.piaar_store_manager.server.model.waybill.WaybillAssembledDto;
import com.piaar_store_manager.server.model.waybill.WaybillFormReadDto;
import com.piaar_store_manager.server.model.waybill.WaybillGetDto;
import com.piaar_store_manager.server.model.waybill.WaybillOptionInfo;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

@Service
public class WaybillService {
    public void getTest(){
        System.out.println("get Test sysout System.out.println");
    }
    public List<WaybillGetDto> getReadExcel(Sheet worksheet){
        List<WaybillGetDto> waybillGetDtos = new ArrayList();

        List<WaybillFormReadDto> waybillFormReadDtos = getWaybillForm(worksheet);
        Collections.sort(waybillFormReadDtos);
        
        Set<String> getDtoSet = new HashSet();
        Set<String> fSet = new HashSet<>();
        Stack<WaybillFormReadDto> fStack = new Stack<>();

        for(int i = 0; i< waybillFormReadDtos.size(); i++){
            String prodName = waybillFormReadDtos.get(i).getProdName();
            if(getDtoSet.add(prodName)){
                WaybillGetDto waybillGetDto = new WaybillGetDto();
                waybillGetDto.setUuid(UUID.randomUUID().toString());
                waybillGetDto.setProdName(prodName);
                waybillGetDto.setList(new ArrayList<>());
                waybillGetDtos.add(waybillGetDto);
                
            }
        }

        // 상품명 + 수취인명 + 주소 + 옵션정보 : 중복이 된다면 수량을 +1 해서 저장
        for (int i = 0; i < waybillFormReadDtos.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(waybillFormReadDtos.get(i).getProdName());
            sb.append(waybillFormReadDtos.get(i).getReceiver());
            sb.append(waybillFormReadDtos.get(i).getDestination());
            sb.append(waybillFormReadDtos.get(i).getOptionInfo());

            String resultString = sb.toString();
            boolean setResult = fSet.add(resultString);

            if (!setResult) {
                WaybillFormReadDto prevData = fStack.pop();
                prevData.setUnit(prevData.getUnit() + waybillFormReadDtos.get(i).getUnit());
                fStack.add(prevData);
            } else {
                fStack.add(waybillFormReadDtos.get(i));
            }
        }

        List<WaybillFormReadDto> fResultList = new ArrayList<>(fStack);

        Set<String> sSet = new HashSet<>();
        Stack<WaybillAssembledDto> sStack = new Stack<>();

        // 상품명 + 수취인명 + 주소 : 중복이 된다면 옵션정보를 추가
        for (int i = 0; i < fResultList.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(fResultList.get(i).getProdName());
            sb.append(fResultList.get(i).getReceiver());
            sb.append(fResultList.get(i).getDestination());

            String resultString = sb.toString();
            boolean setResult = sSet.add(resultString);
            if (!setResult) {
                WaybillAssembledDto prevData = sStack.pop();

                List<WaybillOptionInfo> ois = prevData.getOptionInfos();
                WaybillOptionInfo oi = new WaybillOptionInfo();
                oi.setOptionInfo(fResultList.get(i).getOptionInfo());
                oi.setUnit(fResultList.get(i).getUnit());
                ois.add(oi);
                sStack.add(prevData);
            } else {
                WaybillAssembledDto exd3 = new WaybillAssembledDto();
                exd3.setBuyer(fResultList.get(i).getBuyer());
                exd3.setBuyerContact(fResultList.get(i).getBuyerContact());
                exd3.setDeliveryMessage(fResultList.get(i).getDeliveryMessage());
                exd3.setDestination(fResultList.get(i).getDestination());
                exd3.setPaidTime(fResultList.get(i).getPaidTime());
                exd3.setProdName(fResultList.get(i).getProdName());
                exd3.setProdNo(fResultList.get(i).getProdNo());
                exd3.setProdCode(fResultList.get(i).getProdCode());
                exd3.setReceiver(fResultList.get(i).getReceiver());
                exd3.setReceiverContact1(fResultList.get(i).getReceiverContact1());
                exd3.setReceiverContact2(fResultList.get(i).getReceiverContact2());
                exd3.setZipcode(fResultList.get(i).getZipcode());

                List<WaybillOptionInfo> ois = new ArrayList<>();
                WaybillOptionInfo oi = new WaybillOptionInfo();
                oi.setOptionInfo(fResultList.get(i).getOptionInfo());
                oi.setUnit(fResultList.get(i).getUnit());
                ois.add(oi);

                exd3.setOptionInfos(ois);
                sStack.add(exd3);
            }
        }
        List<WaybillAssembledDto> waybillAssembledDtos= new ArrayList<>(sStack);

        for(int i = 0 ; i < waybillGetDtos.size();i++){
            for(int j = 0 ; j < waybillAssembledDtos.size();j++){
                if(waybillGetDtos.get(i).getProdName().equals(waybillAssembledDtos.get(j).getProdName())){
                    waybillGetDtos.get(i).getList().add(waybillAssembledDtos.get(j));
                }
            }
        }
        return waybillGetDtos;
    }

    private List<WaybillFormReadDto> getWaybillForm(Sheet worksheet){
        List<WaybillFormReadDto> dataList = new ArrayList<>();
        for (int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4

            Row row = worksheet.getRow(i);

            WaybillFormReadDto data = new WaybillFormReadDto();
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
