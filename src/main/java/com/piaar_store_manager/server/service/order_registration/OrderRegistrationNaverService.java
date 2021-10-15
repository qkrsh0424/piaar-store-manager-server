package com.piaar_store_manager.server.service.order_registration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemHansanExcelFormDto;
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
    
    public List<OrderRegistrationNaverFormDto> uploadHansanExcelFile(MultipartFile file) {
        
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<OrderRegistrationNaverFormDto> dtos = this.getOrderRegistrationHansanExcelData(sheet);

        return dtos;
    }

    private List<OrderRegistrationNaverFormDto> getOrderRegistrationHansanExcelData(Sheet worksheet) {
        List<OrderRegistrationNaverFormDto> dtos = new ArrayList<>();

        for(int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

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
}
