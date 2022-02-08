package com.piaar_store_manager.server.service.delivery_ready;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3;
import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemDto;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;
import com.piaar_store_manager.server.service.product_receive.ProductReceiveBusinessService;
import com.piaar_store_manager.server.service.product_release.ProductReleaseBusinessService;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DeliveryReadyPiaarBusinessService {
    private DeliveryReadyPiaarService deliveryReadyPiaarService;
    private ProductReleaseBusinessService productReleaseBusinessService;
    private ProductReceiveBusinessService productReceiveBusinessService;
    private ProductOptionService productOptionService;

    @Autowired
    public DeliveryReadyPiaarBusinessService(
        DeliveryReadyPiaarService deliveryReadyPiaarService,
        ProductReleaseBusinessService productReleaseBusinessService,
        ProductReceiveBusinessService productReceiveBusinessService,
        ProductOptionService productOptionService
    ) {
        this.deliveryReadyPiaarService = deliveryReadyPiaarService;
        this.productReleaseBusinessService = productReleaseBusinessService;
        this.productReceiveBusinessService = productReceiveBusinessService;
        this.productOptionService = productOptionService;
    }

    // AWS S3
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;
    
    @Value("${file.upload-dir}")
    String fileLocation;
    
    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    // Excel file extension.
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
     * <b>Upload Excel File</b>
     * <p>
     * 엑셀 파일을 업로드하여 화면에 출력한다.
     * 
     * @param file : MultipartFile
     * @return List::DeliveryReadyPiaarItemDto::
     * @throws IOException
     * @see DeliveryReadyNaverBusinessService#getDeliveryReadyExcelForm
     */
    public List<DeliveryReadyPiaarItemDto> uploadDeliveryReadyExcelFile(MultipartFile file){
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        // TODO : 타입체크 메서드 구현해야됨.
        Sheet sheet = workbook.getSheetAt(0);

        List<DeliveryReadyPiaarItemDto> dtos = this.getDeliveryReadyExcelForm(sheet);
        return dtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * 선택된 엑셀파일(네이버 배송준비 엑셀)의 데이터들을 Dto로 변환한다.
     * 
     * @param worksheet : Sheet
     * @return List::DeliveryReadyPiaarItemDto::
     */
    private List<DeliveryReadyPiaarItemDto> getDeliveryReadyExcelForm(Sheet worksheet) {
        List<DeliveryReadyPiaarItemDto> dtos = new ArrayList<>();

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);

            DeliveryReadyPiaarItemDto dto = DeliveryReadyPiaarItemDto.builder().id(UUID.randomUUID())
                    .uniqueCode(row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "")
                    .orderNumber1(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                    .orderNumber2(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "")
                    .orderNumber3(row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "")
                    .prodName(row.getCell(4).getStringCellValue())
                    .optionName(row.getCell(5).getStringCellValue())
                    .unit((int) row.getCell(6).getNumericCellValue())
                    .receiver(row.getCell(7).getStringCellValue())
                    .receiverContact1(row.getCell(8).getStringCellValue())
                    .receiverContact2(row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "")
                    .destination(row.getCell(10).getStringCellValue())
                    .zipCode(row.getCell(11) != null ? row.getCell(11).getStringCellValue() : "")
                    .transportType(row.getCell(12) != null ? row.getCell(12).getStringCellValue() : "")
                    .deliveryMessage(row.getCell(13) != null ? row.getCell(13).getStringCellValue() : "")
                    .prodUniqueNumber1(row.getCell(14) != null ? row.getCell(14).getStringCellValue() : "")
                    .prodUniqueNumber2(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "")
                    .optionUniqueNumber1(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                    .optionUniqueNumber2(row.getCell(17) != null ? row.getCell(17).getStringCellValue() : "")
                    .prodCode(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "")
                    .optionCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue() : "")
                    .managementMemo1(row.getCell(20) != null ? row.getCell(20).getStringCellValue() : "")
                    .managementMemo2(row.getCell(21) != null ? row.getCell(21).getStringCellValue() : "")
                    .managementMemo3(row.getCell(22) != null ? row.getCell(22).getStringCellValue() : "")
                    .managementMemo4(row.getCell(23) != null ? row.getCell(23).getStringCellValue() : "")
                    .managementMemo5(row.getCell(24) != null ? row.getCell(24).getStringCellValue() : "")
                    .managementMemo6(row.getCell(25) != null ? row.getCell(25).getStringCellValue() : "")
                    .managementMemo7(row.getCell(26) != null ? row.getCell(26).getStringCellValue() : "")
                    .managementMemo8(row.getCell(27) != null ? row.getCell(27).getStringCellValue() : "")
                    .managementMemo9(row.getCell(28) != null ? row.getCell(28).getStringCellValue() : "")
                    .managementMemo10(row.getCell(29) != null ? row.getCell(29).getStringCellValue() : "")
                    .managementMemo11(row.getCell(30) != null ? row.getCell(30).getStringCellValue() : "")
                    .managementMemo12(row.getCell(31) != null ? row.getCell(31).getStringCellValue() : "")
                    .managementMemo13(row.getCell(32) != null ? row.getCell(32).getStringCellValue() : "")
                    .managementMemo14(row.getCell(33) != null ? row.getCell(33).getStringCellValue() : "")
                    .managementMemo15(row.getCell(34) != null ? row.getCell(34).getStringCellValue() : "")
                    .managementMemo16(row.getCell(35) != null ? row.getCell(35).getStringCellValue() : "")
                    .managementMemo17(row.getCell(36) != null ? row.getCell(36).getStringCellValue() : "")
                    .managementMemo18(row.getCell(37) != null ? row.getCell(37).getStringCellValue() : "")
                    .managementMemo19(row.getCell(38) != null ? row.getCell(38).getStringCellValue() : "")
                    .managementMemo20(row.getCell(39) != null ? row.getCell(39).getStringCellValue() : "")
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }
}
