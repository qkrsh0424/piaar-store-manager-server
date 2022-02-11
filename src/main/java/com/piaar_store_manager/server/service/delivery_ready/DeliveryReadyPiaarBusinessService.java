package com.piaar_store_manager.server.service.delivery_ready;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3;
import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemDto;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.PiaarItemDto;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.PiaarUploadDetailDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyFileDto;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.model.delivery_ready.piaar.entity.DeliveryReadyPiaarItemEntity;
import com.piaar_store_manager.server.model.file_upload.FileUploadResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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


    @Autowired
    public DeliveryReadyPiaarBusinessService(
        DeliveryReadyPiaarService deliveryReadyPiaarService
    ) {
        this.deliveryReadyPiaarService = deliveryReadyPiaarService;
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

    private final Integer PIAAR_EXCEL_HEADER_SIZE = 40;

    private final List<String> PIAAR_EXCEL_HEADER_LIST = Arrays.asList(
            "피아르 고유번호",
            "주문번호1",
            "주문번호2",
            "주문번호3",
            "상품명",
            "옵션명",
            "수량",
            "수취인명",
            "전화번호1",
            "전화번호2",
            "주소",
            "우편번호",
            "배송방식",
            "배송메세지",
            "상품고유번호1",
            "상품고유번호2",
            "옵션고유번호1",
            "옵션고유번호2",
            "피아르 상품코드",
            "피아르 옵션코드",
            "관리메모1",
            "관리메모2",
            "관리메모3",
            "관리메모4",
            "관리메모5",
            "관리메모6",
            "관리메모7",
            "관리메모8",
            "관리메모9",
            "관리메모10",
            "관리메모11",
            "관리메모12",
            "관리메모13",
            "관리메모14",
            "관리메모15",
            "관리메모16",
            "관리메모17",
            "관리메모18",
            "관리메모19",
            "관리메모20");

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
     * @return DeliveryReadyPiaarItemDto
     * @throws IOException
     * @see DeliveryReadyPiaarBusinessService#getDeliveryReadyExcelForm
     */
    public DeliveryReadyPiaarItemDto uploadDeliveryReadyExcelFile(MultipartFile file){
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        // TODO : 타입체크 메서드 구현해야됨.
        Sheet sheet = workbook.getSheetAt(0);

        DeliveryReadyPiaarItemDto dtos = this.getDeliveryReadyExcelForm(sheet);
        return dtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * 선택된 엑셀파일(네이버 배송준비 엑셀)의 데이터들을 Dto로 변환한다.
     * 
     * @param worksheet : Sheet
     * @return DeliveryReadyPiaarItemDto
     */
    private DeliveryReadyPiaarItemDto getDeliveryReadyExcelForm(Sheet worksheet) {
        DeliveryReadyPiaarItemDto itemDto = new DeliveryReadyPiaarItemDto();

        for (int i = 0; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if(row == null) break;
            
            List<PiaarItemDto> itemDtos = new ArrayList<>();
            for(int j = 0; j < PIAAR_EXCEL_HEADER_SIZE; j++) {
                Cell cell = row.getCell(j);

                Object cellValue = new Object();
                if(cell == null || cell.getCellType().equals(CellType.BLANK)){
                    cellValue = "";
                }else if(cell.getCellType().equals(CellType.NUMERIC)){
                    cellValue = cell.getNumericCellValue();
                }else{
                    cellValue = cell.getStringCellValue();
                }

                // 피아르 양식과 다른 헤더를 가진다면
                if (i == 0 && !PIAAR_EXCEL_HEADER_LIST.get(j).equals(cellValue.toString())) {
                    throw new IllegalArgumentException();
                }

                PiaarItemDto piaarItemDto = PiaarItemDto.builder().id(UUID.randomUUID()).cellNumber(j).cellValue(cellValue).build();
                itemDtos.add(piaarItemDto);
            }

            PiaarUploadDetailDto detailDto = PiaarUploadDetailDto.builder().details(itemDtos).build();
            itemDto = DeliveryReadyPiaarItemDto.builder().id(UUID.randomUUID()).uploadDetail(detailDto).build();
        }
        return itemDto;
    }

    /**
     * <b>S3 Upload Setting Related Method</b>
     * <p>
     * AWS S3 설정 메소드.
     *
     * @param accessKey : String
     * @param secretKey : String
     */
    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    /**
     * <b>S3 Upload & DB Insert Related Method</b>
     * <p>
     * 업로드된 엑셀파일을 S3 및 DB에 저장한다.
     *
     * @param file : MultipartFile
     * @param userId : UUID
     * @return FileUploadResponse
     * @throws IllegalStateException
     * @see DeliveryReadyPiaarBusinessService#createDeliveryReadyExcelFile
     * @see DeliveryReadyPiaarBusinessService#createDeliveryReadyExcelItem
     */
    @Transactional
    public FileUploadResponse storeDeliveryReadyExcelFile(MultipartFile file, UUID userId) {
        String fileName = file.getOriginalFilename();
        String newFileName = "[PIAAR_delivery_ready]" + UUID.randomUUID().toString().replaceAll("-", "") + fileName;
        String uploadPath = bucket + "/piaar-order";

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getSize());

        try{
            // AWS S3 업로드
            s3Client.putObject(new PutObjectRequest(uploadPath, newFileName, file.getInputStream(), objMeta).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalStateException();
        }

        // 파일 저장
        DeliveryReadyFileDto fileDto = this.createDeliveryReadyExcelFile(s3Client.getUrl(uploadPath, newFileName).toString(), newFileName, (int)file.getSize(), userId);
        // 데이터 저장
        this.createDeliveryReadyExcelItem(file, fileDto, userId);
                                                      
        return new FileUploadResponse(newFileName, s3Client.getUrl(uploadPath, newFileName).toString(), file.getContentType(), file.getSize());
    }

    /**
     * <b>Create FileDto Method</b>
     * <p>
     * 파일 
     *
     * @param filePath : String
     * @param fileName : String
     * @param fileSize : Integer
     * @param userId : UUID
     * @see DeliveryReadyPiaarService#createFile
     * @see DeliveryReadyFileDto#toDto
     * @return DeliveryReadyFileDto
     */
    public DeliveryReadyFileDto createDeliveryReadyExcelFile(String filePath, String fileName, Integer fileSize, UUID userId) {
        // File data 생성 및 저장
        DeliveryReadyFileDto fileDto = DeliveryReadyFileDto.builder()
            .id(UUID.randomUUID())
            .filePath(filePath)
            .fileName(fileName)
            .fileSize(fileSize)
            .fileExtension(FilenameUtils.getExtension(fileName))
            .createdAt(DateHandler.getCurrentDate2())
            .createdBy(userId)
            .deleted(false)
            .build();

        DeliveryReadyFileEntity entity = deliveryReadyPiaarService.saveFile(DeliveryReadyFileEntity.toEntity(fileDto));
        DeliveryReadyFileDto dto = DeliveryReadyFileDto.toDto(entity);
        return dto;
    }

    /**
     * <b>Create Excel Workbook Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 정보를 저장한다.
     *
     * @param file : MultipartFile
     * @param fileDto : DeliveryReadyFileDto
     * @throws IllegalArgumentException
     * @see DeliveryReadyPiaarBusinessService#getDeliveryReadyPiaarExcelItem
     * @see DeliveryReadyPiaarItemEntity#toEntity
     * @see DeliveryReadyPiaarService#saveItem
     */
    public void createDeliveryReadyExcelItem(MultipartFile file, DeliveryReadyFileDto fileDto, UUID userId) {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        DeliveryReadyPiaarItemDto dto = this.getDeliveryReadyPiaarExcelItem(sheet, fileDto, userId);

        DeliveryReadyPiaarItemEntity entities = DeliveryReadyPiaarItemEntity.toEntity(dto);
        deliveryReadyPiaarService.saveItem(entities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 중복을 제거하여 DB에 저장한다.
     *
     * @param worksheet : Sheet
     * @param fileDto : DeliveryReadyFileDto
     */
    private DeliveryReadyPiaarItemDto getDeliveryReadyPiaarExcelItem(Sheet worksheet, DeliveryReadyFileDto fileDto, UUID userId) {
        DeliveryReadyPiaarItemDto itemDto = new DeliveryReadyPiaarItemDto();
        LocalDateTime createdAt = fileDto.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        for (int i = 0; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if(row == null) break;
            
            List<PiaarItemDto> itemDtos = new ArrayList<>();
            for(int j = 0; j < PIAAR_EXCEL_HEADER_SIZE; j++) {
                Cell cell = row.getCell(j);

                Object cellValue = new Object();
                if(cell == null || cell.getCellType().equals(CellType.BLANK)){
                    cellValue = "";
                }else if(cell.getCellType().equals(CellType.NUMERIC)){
                    cellValue = cell.getNumericCellValue();
                }else{
                    cellValue = cell.getStringCellValue();
                }

                // 피아르 양식과 다른 헤더를 가진다면
                if (i == 0 && !PIAAR_EXCEL_HEADER_LIST.get(j).equals(cellValue.toString())) {
                    throw new IllegalArgumentException();
                }

                PiaarItemDto piaarItemDto = PiaarItemDto.builder().id(UUID.randomUUID()).cellNumber(j).cellValue(cellValue).build();
                itemDtos.add(piaarItemDto);
            }

            PiaarUploadDetailDto detailDto = PiaarUploadDetailDto.builder().details(itemDtos).build();
            itemDto = DeliveryReadyPiaarItemDto.builder()
                .id(UUID.randomUUID())
                .uploadDetail(detailDto)
                .soldYn("n")
                .releasedYn("n")
                .stockReflectedYn("n")
                .createdAt(createdAt)
                .createdBy(userId)
                .deliveryReadyFileCid(fileDto.getCid())
                .build();
        }

        return itemDto;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 유저가 업로드한 엑셀을 전체 가져온다.
     *
     * @return List::DeliveryReadyPiaarItemDto::
     * @see DeliveryReadyPiaarService#searchOrderListByUser
     * @see DeliveryReadyPiaarItemDto#toDto
     */
    public List<DeliveryReadyPiaarItemDto> getDeliveryReadyViewOrderData(UUID userId) {
        List<DeliveryReadyPiaarItemEntity> itemEntities = deliveryReadyPiaarService.searchOrderListByUser(userId);
        List<DeliveryReadyPiaarItemDto> itemDtos = itemEntities.stream().map(r -> DeliveryReadyPiaarItemDto.toDto(r)).collect(Collectors.toList());
        return itemDtos;
    }
}
