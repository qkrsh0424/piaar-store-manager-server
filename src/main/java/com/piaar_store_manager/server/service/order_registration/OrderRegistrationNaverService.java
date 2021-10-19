package com.piaar_store_manager.server.service.order_registration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemTailoExcelFormDto;
import com.piaar_store_manager.server.model.order_registration.naver.OrderRegistrationNaverFormDto;
import com.piaar_store_manager.server.model.order_registration.naver.OrderRegistrationTailoDownloadFormDto;

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
    public List<OrderRegistrationNaverFormDto> uploadHansanExcelFile(MultipartFile file) {
        
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<OrderRegistrationNaverFormDto> dtos = new ArrayList<>();

        for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);

            if(row == null) break;

            // '송장번호 존재, 총 상품주문번호 존재, 네이버' 데이터 추출
            String transportNumber = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : null;
            String allProdOrderNumber = row.getCell(17) != null ? row.getCell(17).getStringCellValue() : null;
            String platformName = row.getCell(18) != null ? row.getCell(18).getStringCellValue() : null;

            if(platformName != null && platformName.equals("네이버") 
                && transportNumber != null && allProdOrderNumber != null) {

                // 총 상품주문번호 '/' 분리
                String[] prodOrderNumber = allProdOrderNumber.split("/");

                OrderRegistrationNaverFormDto dto = OrderRegistrationNaverFormDto.builder()
                    .prodOrderNumber(prodOrderNumber[0])
                    .transportType("택배,등기,소포")
                    .deliveryService("롯데택배")
                    .transportNumber(transportNumber)
                    .build();

                dtos.add(dto);

                
                // 합배송 상품의 데이터를 생성한다
                for(int j = 1; j < prodOrderNumber.length; j++) {
                    OrderRegistrationNaverFormDto combinedDto = OrderRegistrationNaverFormDto.builder()
                        .prodOrderNumber(prodOrderNumber[j])
                        .transportType("택배,등기,소포")
                        .deliveryService("롯데택배")
                        .transportNumber(transportNumber)
                        .build();

                    dtos.add(combinedDto);
                }
            }
        }
        return dtos;
    }

    // 피아르 테일로 발주서 다운 엑셀파일 - 엑셀1
    public List<DeliveryReadyItemTailoExcelFormDto> uploadSendedTailoExcelFile(MultipartFile file) {
        
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
                .receiver(row.getCell(5) != null ? row.getCell(5).getStringCellValue() : null)
                .receiverContact1(row.getCell(6) != null ? row.getCell(6).getStringCellValue() : null)
                .prodUniqueCode(row.getCell(0) != null ? row.getCell(0).getStringCellValue() : null)
                .unit(row.getCell(2) != null ? (int)(row.getCell(2).getNumericCellValue()) : null)
                .destination1(row.getCell(9) != null ? row.getCell(9).getStringCellValue() : null)
                .orderNumber(row.getCell(12) != null ? row.getCell(12).getStringCellValue() : null)
                .managementMemo1(row.getCell(13) != null ? row.getCell(13).getStringCellValue() : null)
                .managementMemo2(row.getCell(14) != null ? row.getCell(14).getStringCellValue() : null)
                .managementMemo3(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : null)
                .build();

            dtos.add(dto);
        }
        return dtos;
    }

    // 테일로에서 송장번호 기입한 엑셀파일 - 엑셀2 업로드
    public List<DeliveryReadyItemTailoExcelFormDto> uploadReceivedTailoExcelFile(MultipartFile file) {
        
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

    /**
    * 1. 엑셀1과 엑셀2가 포함된 dto 파라미터로 전달받음
    * 2. 엑셀1의 관리메모3으로 네이버 데이터만 뽑기, 엑셀2의 송장번호와 관리메모3으로 송장번호 존재, 네이버 데이터만 뽑기
    * 3. 엑셀2와 동일한 정보(받는사람 + 주소 + 고유코드)가 기입된 데이터만 송장번호 추가해주면 됨. 다른 합배송 상품들은 항목을 합치지 않기 때문.
         엑셀1 데이터(합배송상품) - 송장번호 기입
    * 4. 네이버 형식으로 데이터 생성.
    *  + (같은 구매자 - 다른상품 주문 시, 같은 구매자 - 같은 상품 다른 옵션 주문 시 합배송으로 묶이면 따로 표시없고, 운송장번호가 동일하게 나온다.)
    */
    public List<OrderRegistrationNaverFormDto> changeNaverFormDtoByTailoFormDto(OrderRegistrationTailoDownloadFormDto tailoDto) {
        List<OrderRegistrationNaverFormDto> naverDtos = new ArrayList<>();

        // 네이버 대량등록에 등록될 테일로 발주 데이터 추출
        List<DeliveryReadyItemTailoExcelFormDto> orderDtos = this.getOrderRegistrationDtos(tailoDto.getSendedDto(), tailoDto.getReceivedDto());

        // 5.
        for(int i = 0; i < orderDtos.size(); i++) {
            OrderRegistrationNaverFormDto dto = OrderRegistrationNaverFormDto.builder()
                    .prodOrderNumber(orderDtos.get(i).getManagementMemo2())
                    .transportType("택배,등기,소포")
                    .deliveryService(orderDtos.get(i).getDeliveryService())
                    .transportNumber(orderDtos.get(i).getTransportNumber())
                    .build();
                    
            naverDtos.add(dto);
        }
        return naverDtos;
    }

    // 네이버 대량등록에 등록될 데이터 추출 + 합배송상품들 나누기
    // 1. 걸러진 receivedDtos를 모두 추가
    // 2. senedDtos에서 주문번호가 동일한 아이들 중 이미 추가 되지 않은 애들(상품관리번호이 고유한)만 송장번호를 기입해서 추가
    // 3. 추가된 애들 반환
    public List<DeliveryReadyItemTailoExcelFormDto> getOrderRegistrationDtos(List<DeliveryReadyItemTailoExcelFormDto> sendedDtos, List<DeliveryReadyItemTailoExcelFormDto> receivedDtos) {
        List<DeliveryReadyItemTailoExcelFormDto> tailoReceivedDtos = new ArrayList<>();     // 등록해야 할 발주 데이터
        Map<String, DeliveryReadyItemTailoExcelFormDto> transportNumInfo = new HashMap<>();  // <주문번호, receivedDtos 데이터>

        for(int i = 0; i < receivedDtos.size(); i++) {
            if(receivedDtos.get(i).getManagementMemo3() != null && receivedDtos.get(i).getManagementMemo3().equals("네이버")
                    && receivedDtos.get(i).getTransportNumber() != null){
                
                transportNumInfo.put(receivedDtos.get(i).getOrderNumber(), receivedDtos.get(i));
                tailoReceivedDtos.add(receivedDtos.get(i));
            }
        }

        for(int i = 0; i < sendedDtos.size(); i++) {
            // sendDtos에서 주문번호가 동일한 데이터 중 이미 추가 되지 않은 데이터(상품관리번호가 고유한) 값만 송장번호와 택배사 기입해서 추가
            if(sendedDtos.get(i).getManagementMemo3() != null && sendedDtos.get(i).getManagementMemo3().equals("네이버")
                        && transportNumInfo.get(sendedDtos.get(i).getOrderNumber()) != null
                        && !transportNumInfo.get(sendedDtos.get(i).getOrderNumber()).getManagementMemo2().equals(sendedDtos.get(i).getManagementMemo2())) {

                    sendedDtos.get(i).setTransportNumber(transportNumInfo.get(sendedDtos.get(i).getOrderNumber()).getTransportNumber());
                    sendedDtos.get(i).setDeliveryService(transportNumInfo.get(sendedDtos.get(i).getOrderNumber()).getDeliveryService());
                    tailoReceivedDtos.add(sendedDtos.get(i));
            }
        }

        return tailoReceivedDtos;
    }
}
