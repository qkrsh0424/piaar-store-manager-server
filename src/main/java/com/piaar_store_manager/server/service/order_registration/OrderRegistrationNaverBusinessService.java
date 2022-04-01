package com.piaar_store_manager.server.service.order_registration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.piaar_store_manager.server.model.order_registration.naver.OrderRegistrationHansanExcelFormDto;
import com.piaar_store_manager.server.model.order_registration.naver.OrderRegistrationNaverFormDto;
import com.piaar_store_manager.server.model.order_registration.naver.OrderRegistrationTailoExcelFormDto;
import com.piaar_store_manager.server.utils.CustomExcelUtils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OrderRegistrationNaverBusinessService {

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * 업로드된 한산 발주서 양식에서 필요한 데이터만 추출해 dto를 생성한다.
     *
     * @param file : MultipartFile
     * @return List::OrderRegistrationHansanExcelFormDto::
     */
    public List<OrderRegistrationHansanExcelFormDto> uploadHansanExcelFile(MultipartFile file) {
        Integer SHEET_INDEX = 0;
        Workbook workbook = CustomExcelUtils.createWorkBook(file);
        Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
        List<OrderRegistrationHansanExcelFormDto> dtos = new ArrayList<>();

        for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);

            if(row == null) break;
            if(row.getCell(0) == null) break;

            OrderRegistrationHansanExcelFormDto dto = OrderRegistrationHansanExcelFormDto.builder()
                .receiver(row.getCell(0) != null ? row.getCell(0).getStringCellValue() : null)
                .receiverContact1(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : null)
                .prodName(row.getCell(5) != null ? row.getCell(5).getStringCellValue() : null)
                .optionInfo(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : null)
                .unit(row.getCell(9) != null ? (int) (row.getCell(9).getNumericCellValue()) : null)
                .destination(row.getCell(3) != null ? row.getCell(3).getStringCellValue() : null)
                .orderNumber(row.getCell(12) != null ? row.getCell(12).getStringCellValue() : null)
                .optionManagementCode(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : null)
                .prodOrderNumber(row.getCell(13) != null ? row.getCell(13).getStringCellValue() : null)
                .platformName(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : null)
                .transportType("택배,등기,소포")
                .deliveryService("롯데택배")
                .transportNumber(row.getCell(4) != null ? row.getCell(4).getStringCellValue() : null)
                .allProdOrderNumber(row.getCell(17) != null ? row.getCell(17).getStringCellValue() : null)
                .build();

            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * 한산 발주서 양식에서 등록가능한 데이터를 추출한다.
     * 합배송 상품들을 분리하고 발주 데이터를 채워 네이버 대량등록 양식으로 변환한다.
     *
     * @param hansanDtos : List::OrderRegistrationHansanExcelFormDto::
     * @return List::OrderRegistrationNaverFormDto::
     */
    public List<OrderRegistrationNaverFormDto> changeNaverFormDtoByHansanFormDto(List<OrderRegistrationHansanExcelFormDto> hansanDtos) {
        List<OrderRegistrationNaverFormDto> dtos = new ArrayList<>();

        for (OrderRegistrationHansanExcelFormDto hansanDto : hansanDtos) {
            // '송장번호 존재, 총 상품주문번호 존재, 네이버' 데이터 추출
            String transportNumber = hansanDto.getTransportNumber();
            String allProdOrderNumber = hansanDto.getAllProdOrderNumber();
            String platformName = hansanDto.getPlatformName();

            if (platformName != null && platformName.equals("네이버") && transportNumber != null && allProdOrderNumber != null) {
                // 총 상품주문번호 '/' 분리
                String[] prodOrderNumber = allProdOrderNumber.split("/");

                OrderRegistrationNaverFormDto dto = OrderRegistrationNaverFormDto.builder()
                        .prodOrderNumber(prodOrderNumber[0])
                        .transportType(hansanDto.getTransportType())
                        .deliveryService(hansanDto.getDeliveryService())
                        .transportNumber(transportNumber).build();

                dtos.add(dto);

                // 합배송 상품의 데이터를 생성한다
                for (int j = 1; j < prodOrderNumber.length; j++) {
                    OrderRegistrationNaverFormDto combinedDto = OrderRegistrationNaverFormDto.builder()
                            .prodOrderNumber(prodOrderNumber[j])
                            .transportType(hansanDto.getTransportType())
                            .deliveryService(hansanDto.getDeliveryService())
                            .transportNumber(transportNumber).build();

                    dtos.add(combinedDto);
                }
            }
        }
        return dtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * 업로드된 테일로 발주서 양식에서 필요한 데이터만 추출해 dto를 생성한다.
     *
     * @param file : MultipartFile
     * @return List::OrderRegistrationTailoExcelFormDto::
     */
    public List<OrderRegistrationTailoExcelFormDto> uploadTailoExcelFile(MultipartFile file) {
    
        Integer SHEET_INDEX = 0;
        Workbook workbook = CustomExcelUtils.createWorkBook(file);
        Sheet sheet = workbook.getSheetAt(SHEET_INDEX);
        List<OrderRegistrationTailoExcelFormDto> dtos = new ArrayList<>();

        for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            if(row == null) break;

            OrderRegistrationTailoExcelFormDto dto = OrderRegistrationTailoExcelFormDto.builder()
                .receiver(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : null)
                .receiverContact1(row.getCell(11) != null ? row.getCell(11).getStringCellValue() : null)
                .prodUniqueCode(row.getCell(5) != null ? row.getCell(5).getStringCellValue() : null)
                .salesProdName(row.getCell(6) != null ? row.getCell(6).getStringCellValue() : null)
                .unit(row.getCell(7) != null ? (int)(row.getCell(7).getNumericCellValue()) : null)
                .destination1(row.getCell(14) != null ? row.getCell(14).getStringCellValue() : null)
                .orderNumber(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : null)
                .prodMemo1(row.getCell(22) != null ? row.getCell(22).getStringCellValue() : null)
                .prodMemo3(row.getCell(24) != null ? row.getCell(24).getStringCellValue() : null)
                .managementMemo3(row.getCell(19) != null ? row.getCell(19).getStringCellValue() : null)
                .transportType(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : null)
                .deliveryService(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : null)
                .transportNumber(row.getCell(3) != null ? row.getCell(3).getStringCellValue() : null)
                .build();

            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * 테일로 발주서 양식에서 등록가능한 데이터를 추출해 네이버 대량등록 양식으로 변환한다.
     *
     * @param tailoDtos : List::OrderRegistrationTailoExcelFormDto::
     * @return List::OrderRegistrationNaverFormDto::
     */
    public List<OrderRegistrationNaverFormDto> changeNaverFormDtoByTailoFormDto(List<OrderRegistrationTailoExcelFormDto> tailoDtos) {
        List<OrderRegistrationNaverFormDto> dtos = new ArrayList<>();

        for (OrderRegistrationTailoExcelFormDto tailoDto : tailoDtos) {
            // '송장번호 존재, 네이버' 데이터 추출
            String transportNumber = tailoDto.getTransportNumber();
            String platformName = tailoDto.getManagementMemo3();

            if (platformName != null && platformName.equals("네이버") && transportNumber != null) {
                OrderRegistrationNaverFormDto dto = OrderRegistrationNaverFormDto.builder()
                        .prodOrderNumber(tailoDto.getProdMemo1())
                        .transportType(tailoDto.getTransportType())
                        .deliveryService(tailoDto.getDeliveryService())
                        .transportNumber(transportNumber).build();

                dtos.add(dto);
            }
        }
        return dtos;
    }
}
