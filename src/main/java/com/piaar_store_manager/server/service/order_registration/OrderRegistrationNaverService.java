package com.piaar_store_manager.server.service.order_registration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemHansanExcelFormDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemTailoExcelFormDto;
import com.piaar_store_manager.server.model.order_registration.naver.OrderRegistrationNaverFormDto;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OrderRegistrationNaverService {
    
    // excel file extension.
    private final List<String> EXTENSIONS_EXCEL = Arrays.asList("xlsx", "xls");

    /**
     * <b>Extension Check</b>
     * <p>
     * 
     * @param file : MultipartFile
     * @throws FileUploadException
     */
    public void isExcelFile(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase());

        if(EXTENSIONS_EXCEL.contains(extension)){
            return;
        }
        throw new FileUploadException("This is not an excel file.");
    }

    /**
     * 1. 한산 엑셀 업로드
     *  - a. 한산 엑셀에서 송장번호 존재, 총 상품주문번호 존재, 네이버 데이터 뽑아내기
     *  - b. 총 상품주문번호 '/' 분리
     *  - c. 분리된 총 상품주문번호로 상품주문번호 작성, 운송장번호 추가
     *  - d. return dto (업로드한 객체를 네이버 대량등록 dto로 return)
     * 2. 네이버 발주 등록 엑셀 다운로드
     */ 
    public List<DeliveryReadyItemHansanExcelFormDto> uploadHansanExcelFile(MultipartFile file) {
        
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<DeliveryReadyItemHansanExcelFormDto> dtos = new ArrayList<>();

        for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);

            if(row == null) break;
            if(row.getCell(0) == null) break;

            DeliveryReadyItemHansanExcelFormDto dto = DeliveryReadyItemHansanExcelFormDto.builder()
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

    public List<OrderRegistrationNaverFormDto> changeNaverFormDtoByHansanFormDto(List<DeliveryReadyItemHansanExcelFormDto> hansanDtos) {
        List<OrderRegistrationNaverFormDto> dtos = new ArrayList<>();

        for (DeliveryReadyItemHansanExcelFormDto hansanDto : hansanDtos) {

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

    // 테일로에서 송장번호 기입한 엑셀파일 업로드
    public List<DeliveryReadyItemTailoExcelFormDto> uploadTailoExcelFile(MultipartFile file) {
        
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<DeliveryReadyItemTailoExcelFormDto> dtos = new ArrayList<>();

        for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);

            if(row == null) break;

            DeliveryReadyItemTailoExcelFormDto dto = DeliveryReadyItemTailoExcelFormDto.builder()
                .receiver(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : null)
                .receiverContact1(row.getCell(11) != null ? row.getCell(11).getStringCellValue() : null)
                .prodUniqueCode(row.getCell(5) != null ? row.getCell(5).getStringCellValue() : null)
                .salesProdName(row.getCell(6) != null ? row.getCell(6).getStringCellValue() : null)
                .unit(row.getCell(7) != null ? (int)(row.getCell(7).getNumericCellValue()) : null)
                .destination1(row.getCell(14) != null ? row.getCell(14).getStringCellValue() : null)
                .orderNumber(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : null)
                .managementMemo1(row.getCell(17) != null ? row.getCell(17).getStringCellValue() : null)
                .managementMemo2(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : null)
                .managementMemo3(row.getCell(19) != null ? row.getCell(19).getStringCellValue() : null)
                .transportType(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : null)
                .deliveryService(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : null)
                .transportNumber(row.getCell(3) != null ? row.getCell(3).getStringCellValue() : null)
                .build();

            dtos.add(dto);
        }

        return dtos;
    }

    public List<OrderRegistrationNaverFormDto> changeNaverFormDtoByTailoFormDto(List<DeliveryReadyItemTailoExcelFormDto> tailoDtos) {

        List<OrderRegistrationNaverFormDto> dtos = new ArrayList<>();

        for (DeliveryReadyItemTailoExcelFormDto tailoDto : tailoDtos) {

            // '송장번호 존재, 네이버' 데이터 추출
            String transportNumber = tailoDto.getTransportNumber();
            String platformName = tailoDto.getManagementMemo3();

            if (platformName != null && platformName.equals("네이버") && transportNumber != null) {

                OrderRegistrationNaverFormDto dto = OrderRegistrationNaverFormDto.builder()
                        .prodOrderNumber(tailoDto.getManagementMemo2())
                        .transportType(tailoDto.getTransportType())
                        .deliveryService(tailoDto.getDeliveryService())
                        .transportNumber(transportNumber).build();

                dtos.add(dto);
            }
        }

        return dtos;
    }
}
