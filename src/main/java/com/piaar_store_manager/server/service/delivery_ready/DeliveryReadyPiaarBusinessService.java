package com.piaar_store_manager.server.service.delivery_ready;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemDto;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.PiaarItemDto;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.PiaarUploadDetailDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyFileDto;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.model.delivery_ready.piaar.entity.DeliveryReadyPiaarItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.piaar.proj.DeliveryReadyPiaarItemViewProj;
import com.piaar_store_manager.server.model.file_upload.FileUploadResponse;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DeliveryReadyPiaarBusinessService {
    private DeliveryReadyPiaarService deliveryReadyPiaarService;
    private ProductOptionService productOptionService;

    @Autowired
    public DeliveryReadyPiaarBusinessService(
        DeliveryReadyPiaarService deliveryReadyPiaarService,
        ProductOptionService productOptionService
    ) {
        this.deliveryReadyPiaarService = deliveryReadyPiaarService;
        this.productOptionService = productOptionService;
    }

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
     * @return DeliveryReadyPiaarItemDto
     */
    private List<DeliveryReadyPiaarItemDto> getDeliveryReadyExcelForm(Sheet worksheet) {
        List<DeliveryReadyPiaarItemDto> piaarItemDtos = new ArrayList<>();

        Row firstRow = worksheet.getRow(0);
        // 피아르 엑셀 양식 검사
        for(int i = 0; i < PIAAR_EXCEL_HEADER_SIZE; i++) {
            Cell cell = firstRow.getCell(i);
            String headerName = cell != null ? cell.getStringCellValue() : null;
            // 지정된 양식이 아니라면
            if(!PIAAR_EXCEL_HEADER_LIST.get(i).equals(headerName)) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if(row == null) break;
            
            DeliveryReadyPiaarItemDto piaarItemDto = new DeliveryReadyPiaarItemDto();
            List<PiaarItemDto> itemDtos = new ArrayList<>();
            for(int j = 0; j < PIAAR_EXCEL_HEADER_SIZE; j++) {
                Cell cell = row.getCell(j);

                Object cellValue = new Object();
                
                if(cell == null || cell.getCellType().equals(CellType.BLANK)){
                    cellValue = "";
                }else if(cell.getCellType().equals(CellType.NUMERIC)){
                    if (DateUtil.isCellDateFormatted(cell)) {
                            Instant instant = Instant.ofEpochMilli(cell.getDateCellValue().getTime());
                            LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                            // yyyy-MM-dd'T'HH:mm:ss -> yyyy-MM-dd HH:mm:ss로 변경
                            String newDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            cellValue = newDate;
                    } else {
                        cellValue = cell.getNumericCellValue();
                    }
                }else{
                    cellValue = cell.getStringCellValue();
                }

                PiaarItemDto itemDto = PiaarItemDto.builder().id(UUID.randomUUID()).cellNumber(j).cellValue(cellValue).build();
                itemDtos.add(itemDto);
            }
            PiaarUploadDetailDto detailDto = PiaarUploadDetailDto.builder().details(itemDtos).build();
            piaarItemDto = DeliveryReadyPiaarItemDto.builder().id(UUID.randomUUID()).uploadDetail(detailDto).build();

            piaarItemDtos.add(piaarItemDto);
        }
        return piaarItemDtos;
    }

    /**
     * <b>S3 Upload & DB Insert Related Method</b>
     * <p>
     * 업로드된 엑셀파일을 DB에 저장한다.
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
        String urlPath = "";

        // 파일 저장
        DeliveryReadyFileDto fileDto = this.createDeliveryReadyExcelFile(urlPath, newFileName, (int)file.getSize(), userId);
        // 데이터 저장
        this.createDeliveryReadyExcelItem(file, fileDto, userId);
                                                      
        return new FileUploadResponse(newFileName, urlPath, file.getContentType(), file.getSize());
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
     * @see DeliveryReadyPiaarService#saveItemList
     */
    public void createDeliveryReadyExcelItem(MultipartFile file, DeliveryReadyFileDto fileDto, UUID userId) {
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<DeliveryReadyPiaarItemDto> dtos = this.getDeliveryReadyPiaarExcelItem(sheet, fileDto, userId);

        List<DeliveryReadyPiaarItemEntity> entities = dtos.stream().map(r -> DeliveryReadyPiaarItemEntity.toEntity(r)).collect(Collectors.toList());
        deliveryReadyPiaarService.saveItemList(entities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일 데이터들의 중복을 제거하여 DB에 저장한다.
     *
     * @param worksheet : Sheet
     * @param fileDto : DeliveryReadyFileDto
     */
    private List<DeliveryReadyPiaarItemDto> getDeliveryReadyPiaarExcelItem(Sheet worksheet, DeliveryReadyFileDto fileDto, UUID userId) {
        List<DeliveryReadyPiaarItemDto> piaarItemDtos = new ArrayList<>();
        LocalDateTime createdAt = fileDto.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Row firstRow = worksheet.getRow(0);
        // 피아르 엑셀 양식 검사
        for(int i = 0; i < PIAAR_EXCEL_HEADER_SIZE; i++) {
            Cell cell = firstRow.getCell(i);
            String headerName = cell != null ? cell.getStringCellValue() : null;
            // 지정된 양식이 아니라면
            if(!PIAAR_EXCEL_HEADER_LIST.get(i).equals(headerName)) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if(row == null) break;
            
            DeliveryReadyPiaarItemDto piaarItemDto = new DeliveryReadyPiaarItemDto();
            List<PiaarItemDto> itemDtos = new ArrayList<>();
            for(int j = 0; j < PIAAR_EXCEL_HEADER_SIZE; j++) {
                Cell cell = row.getCell(j);
                Object cellValue = new Object();
                
                if(cell == null || cell.getCellType().equals(CellType.BLANK)){
                    // j가 0일 때, 피아르 고유번호 지정
                    if(j == 0) {
                        cellValue = UUID.randomUUID();
                    } else {
                        cellValue = "";
                    }
                }else if(cell.getCellType().equals(CellType.NUMERIC)) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Instant instant = Instant.ofEpochMilli(cell.getDateCellValue().getTime());
                        LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                        // yyyy-MM-dd'T'HH:mm:ss -> yyyy-MM-dd HH:mm:ss로 변경
                        String newDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        cellValue = newDate;
                    } else {
                        cellValue = cell.getNumericCellValue();
                    }
                } else {
                    cellValue = cell.getStringCellValue();
                }

                // 피아르 양식과 다른 헤더를 가진다면
                if (i == 0 && !PIAAR_EXCEL_HEADER_LIST.get(j).equals(cellValue.toString())) {
                    throw new IllegalArgumentException();
                }

                PiaarItemDto itemDto = PiaarItemDto.builder().id(UUID.randomUUID()).cellNumber(j).cellValue(cellValue).build();
                itemDtos.add(itemDto);
            }

            PiaarUploadDetailDto detailDto = PiaarUploadDetailDto.builder().details(itemDtos).build();
            piaarItemDto = DeliveryReadyPiaarItemDto.builder()
                .id(UUID.randomUUID())
                .uploadDetail(detailDto)
                .soldYn("n")
                .releasedYn("n")
                .stockReflectedYn("n")
                .createdAt(createdAt)
                .createdBy(userId)
                .deliveryReadyFileCid(fileDto.getCid())
                .build();

            piaarItemDtos.add(piaarItemDto);
        }
        return piaarItemDtos;
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 유저가 업로드한 엑셀을 전체 가져온다.
     * 피아르 관리코드에 대응하는 데이터들을 반환 Dto에 추가한다.
     *
     * @return List::DeliveryReadyPiaarItemDto::
     * @see DeliveryReadyPiaarService#searchOrderListByUser
     * @see DeliveryReadyPiaarItemDto#toDto
     */
    public List<DeliveryReadyPiaarItemDto> getDeliveryReadyViewOrderData(UUID userId) {
        // 매핑데이터 조회
        List<DeliveryReadyPiaarItemViewProj> itemViewProjs = deliveryReadyPiaarService.findMappingDataByPiaarOptionCodeAndUser(userId);
        List<DeliveryReadyPiaarItemViewResDto> resDtos = itemViewProjs.stream().map(r -> DeliveryReadyPiaarItemViewResDto.toResDto(r)).collect(Collectors.toList());

        // 카테고리명, 상품명, 상품관리명, 옵션명, 옵션관리명 추가
        List<DeliveryReadyPiaarItemDto> addedPiaarItemDtos = this.addDeliveryReadyViewMappingData(resDtos);
        // 옵션재고수량 추가
        List<DeliveryReadyPiaarItemDto> allPiaarViewItemDtos= this.getOptionStockUnit(addedPiaarItemDtos);
        return allPiaarViewItemDtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * resDtos의 피아르 옵션코드에 대응하는 카테고리명, 상품명, 상품관리명, 옵션명, 옵션관리명을 resDtos 데이터에 추가한다.
     *
     * @param resDtos : List::DeliveryReadyPiaarItemDto::
     * @return List::DeliveryReadyPiaarItemDto::
     * @see ProductOptionService#searchListByProductListOptionCode
     */
    public List<DeliveryReadyPiaarItemDto> addDeliveryReadyViewMappingData(List<DeliveryReadyPiaarItemViewResDto> resDtos) {
        List<DeliveryReadyPiaarItemDto> resultDtos = resDtos.stream().map(r -> {
            List<PiaarItemDto> result = r.getDeliveryReadyItem().getUploadDetail().getDetails();
            PiaarItemDto piaarItemDto = new PiaarItemDto();

            // 카테고리명
            piaarItemDto = PiaarItemDto.builder().id(UUID.randomUUID()).cellNumber(PIAAR_EXCEL_HEADER_SIZE + 0).cellValue(r.getCategoryName()).build();
            result.add(piaarItemDto);

            // 상품명
            piaarItemDto = PiaarItemDto.builder().id(UUID.randomUUID()).cellNumber(PIAAR_EXCEL_HEADER_SIZE + 1).cellValue(r.getProdDefaultName()).build();
            result.add(piaarItemDto);

            // 상품관리명
            piaarItemDto = PiaarItemDto.builder().id(UUID.randomUUID()).cellNumber(PIAAR_EXCEL_HEADER_SIZE + 2).cellValue(r.getProdManagementName()).build();
            result.add(piaarItemDto);

            // 옵션명
            piaarItemDto = PiaarItemDto.builder().id(UUID.randomUUID()).cellNumber(PIAAR_EXCEL_HEADER_SIZE + 3).cellValue(r.getOptionDefaultName()).build();
            result.add(piaarItemDto);

            // 옵션관리명
            piaarItemDto = PiaarItemDto.builder().id(UUID.randomUUID()).cellNumber(PIAAR_EXCEL_HEADER_SIZE + 4).cellValue(r.getOptionManagementName()).build();
            result.add(piaarItemDto);

            r.getDeliveryReadyItem().getUploadDetail().setDetails(result);
            return r.getDeliveryReadyItem();
        }).collect(Collectors.toList());

        return resultDtos;
    }

    /**
     * <b>Data Processing Related Method</b>
     * <p>
     * resDtos의 피아르 옵션코드에 대응하는 옵션의 StockSumUnit(총 입고 수량 - 총 출고 수량)을 가져와 resDtos 데이터에 추가한다.
     *
     * @param resDtos : List::DeliveryReadyPiaarItemDto::
     * @return List::DeliveryReadyPiaarItemDto::
     * @see ProductOptionService#searchListByProductListOptionCode
     */
    public List<DeliveryReadyPiaarItemDto> getOptionStockUnit(List<DeliveryReadyPiaarItemDto> resDtos) {
        List<String> optionCodes = resDtos.stream().map(r -> r.getUploadDetail().getDetails().get(19).getCellValue().toString()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByProductListOptionCode(optionCodes);
    
        // 옵션 재고수량을 StockSumUnit(총 입고 수량 - 총 출고 수량)으로 변경
        List<DeliveryReadyPiaarItemDto>  itemDtos = resDtos.stream().map(resDto -> {
            // 옵션 코드와 동일한 상품의 재고수량을 변경한다
            optionGetDtos.stream().forEach(option -> {
                List<PiaarItemDto> result = resDto.getUploadDetail().getDetails();
    
                if(resDto.getUploadDetail().getDetails().get(19).getCellValue().equals(option.getCode())) {
                    PiaarItemDto piaarItemDto  = PiaarItemDto.builder().id(UUID.randomUUID()).cellNumber(PIAAR_EXCEL_HEADER_SIZE + 5).cellValue(option.getStockSumUnit()).build();
                    result.add(piaarItemDto);
                }
            });
            return resDto;
        }).collect(Collectors.toList());

        return itemDtos;
    }
}
