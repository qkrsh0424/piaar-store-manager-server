package com.piaar_store_manager.server.service.delivery_ready;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemDto;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemViewResDto;

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
import com.piaar_store_manager.server.model.delivery_ready.piaar.repository.DeliveryReadyPiaarItemRepository;
import com.piaar_store_manager.server.model.delivery_ready.piaar.vo.DeliveryReadyPiaarUploadedExcelItemVo;
import com.piaar_store_manager.server.model.delivery_ready.piaar.vo.DeliveryReadyPiaarViewExcelItemVo;
import com.piaar_store_manager.server.model.delivery_ready.piaar.vo.PiaarCombinedDeliveryDetailVo;
import com.piaar_store_manager.server.model.delivery_ready.piaar.vo.PiaarCombinedDeliveryExcelItemVo;
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
    private final Integer PIAAR_MEMO_START_INDEX = 20;

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
    public List<DeliveryReadyPiaarUploadedExcelItemVo> uploadDeliveryReadyExcelFile(MultipartFile file){
        Workbook workbook = null;
        try{
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        // TODO : 타입체크 메서드 구현해야됨.
        Sheet sheet = workbook.getSheetAt(0);

        List<DeliveryReadyPiaarUploadedExcelItemVo> dtos = this.getDeliveryReadyExcelForm(sheet);
        return dtos;
    }

    private List<DeliveryReadyPiaarUploadedExcelItemVo> getDeliveryReadyExcelForm(Sheet worksheet) {
        List<DeliveryReadyPiaarUploadedExcelItemVo> itemVos = new ArrayList<>();
        
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

            Object cellValue = new Object();
            List<String> customManagementMemo = new ArrayList<>();

            // type check and data setting of managementMemo1~20.
            for(int j = PIAAR_MEMO_START_INDEX; j < PIAAR_EXCEL_HEADER_SIZE; j++) {
                Cell cell = row.getCell(j);

                if (cell == null || cell.getCellType().equals(CellType.BLANK)) {
                    cellValue = "";
                } else if (cell.getCellType().equals(CellType.NUMERIC)) {
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
                customManagementMemo.add(cellValue.toString());
            }

            DeliveryReadyPiaarUploadedExcelItemVo excelDto = DeliveryReadyPiaarUploadedExcelItemVo.builder()
                .uniqueCode(UUID.randomUUID())
                .orderNumber1(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                .orderNumber2(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "")
                .orderNumber3(row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "")
                .prodName(row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "")
                .optionName(row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "")
                .unit(row.getCell(6) != null ? (int) row.getCell(6).getNumericCellValue() : 0)
                .receiver(row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "")
                .receiverContact1(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "")
                .receiverContact2(row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "")
                .destination(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
                .zipCode(row.getCell(11) != null ? row.getCell(11).getStringCellValue() : "")
                .transportType(row.getCell(12) != null ? row.getCell(12).getStringCellValue() : "")
                .deliveryMessage(row.getCell(13) != null ? row.getCell(13).getStringCellValue() : "")
                .prodUniqueNumber1(row.getCell(14) != null ? row.getCell(14).getStringCellValue() : "")
                .prodUniqueNumber2(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "")
                .optionUniqueNumber1(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                .optionUniqueNumber2(row.getCell(17) != null ? row.getCell(17).getStringCellValue() : "")
                .prodCode(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "")
                .optionCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue() : "")
                .managementMemo1(customManagementMemo.get(0))
                .managementMemo2(customManagementMemo.get(1))
                .managementMemo3(customManagementMemo.get(2))
                .managementMemo4(customManagementMemo.get(3))
                .managementMemo5(customManagementMemo.get(4))
                .managementMemo6(customManagementMemo.get(5))
                .managementMemo7(customManagementMemo.get(6))
                .managementMemo8(customManagementMemo.get(7))
                .managementMemo9(customManagementMemo.get(8))
                .managementMemo10(customManagementMemo.get(9))
                .managementMemo11(customManagementMemo.get(10))
                .managementMemo12(customManagementMemo.get(11))
                .managementMemo13(customManagementMemo.get(12))
                .managementMemo14(customManagementMemo.get(13))
                .managementMemo15(customManagementMemo.get(14))
                .managementMemo16(customManagementMemo.get(15))
                .managementMemo17(customManagementMemo.get(16))
                .managementMemo18(customManagementMemo.get(17))
                .managementMemo19(customManagementMemo.get(18))
                .managementMemo20(customManagementMemo.get(19))
                .build();

            itemVos.add(excelDto);
        }
        return itemVos;
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
        List<DeliveryReadyPiaarItemDto> itemDtos = new ArrayList<>();
        LocalDateTime createdAt = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

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

            Object cellValue = new Object();
            List<String> customManagementMemo = new ArrayList<>();

            // type check and data setting of managementMemo1~20.
            for(int j = PIAAR_MEMO_START_INDEX; j < PIAAR_EXCEL_HEADER_SIZE; j++) {
                Cell cell = row.getCell(j);

                if (cell == null || cell.getCellType().equals(CellType.BLANK)) {
                    cellValue = "";
                } else if (cell.getCellType().equals(CellType.NUMERIC)) {
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
                customManagementMemo.add(cellValue.toString());
            }

            DeliveryReadyPiaarItemDto excelDto = DeliveryReadyPiaarItemDto.builder()
                .id(UUID.randomUUID())
                .uniqueCode(UUID.randomUUID())
                .orderNumber1(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                .orderNumber2(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "")
                .orderNumber3(row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "")
                .prodName(row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "")
                .optionName(row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "")
                .unit(row.getCell(6) != null ? (int) row.getCell(6).getNumericCellValue() : 0)
                .receiver(row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "")
                .receiverContact1(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "")
                .receiverContact2(row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "")
                .destination(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
                .zipCode(row.getCell(11) != null ? row.getCell(11).getStringCellValue() : "")
                .transportType(row.getCell(12) != null ? row.getCell(12).getStringCellValue() : "")
                .deliveryMessage(row.getCell(13) != null ? row.getCell(13).getStringCellValue() : "")
                .prodUniqueNumber1(row.getCell(14) != null ? row.getCell(14).getStringCellValue() : "")
                .prodUniqueNumber2(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "")
                .optionUniqueNumber1(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                .optionUniqueNumber2(row.getCell(17) != null ? row.getCell(17).getStringCellValue() : "")
                .prodCode(row.getCell(18) != null ? row.getCell(18).getStringCellValue() : "")
                .optionCode(row.getCell(19) != null ? row.getCell(19).getStringCellValue() : "")
                .managementMemo1(customManagementMemo.get(0))
                .managementMemo2(customManagementMemo.get(1))
                .managementMemo3(customManagementMemo.get(2))
                .managementMemo4(customManagementMemo.get(3))
                .managementMemo5(customManagementMemo.get(4))
                .managementMemo6(customManagementMemo.get(5))
                .managementMemo7(customManagementMemo.get(6))
                .managementMemo8(customManagementMemo.get(7))
                .managementMemo9(customManagementMemo.get(8))
                .managementMemo10(customManagementMemo.get(9))
                .managementMemo11(customManagementMemo.get(10))
                .managementMemo12(customManagementMemo.get(11))
                .managementMemo13(customManagementMemo.get(12))
                .managementMemo14(customManagementMemo.get(13))
                .managementMemo15(customManagementMemo.get(14))
                .managementMemo16(customManagementMemo.get(15))
                .managementMemo17(customManagementMemo.get(16))
                .managementMemo18(customManagementMemo.get(17))
                .managementMemo19(customManagementMemo.get(18))
                .managementMemo20(customManagementMemo.get(19))
                .soldYn("n")
                .releasedYn("n")
                .stockReflectedYn("n")
                .createdAt(createdAt)
                .createdBy(userId)
                .deliveryReadyFileCid(fileDto.getCid())
                .build();

            itemDtos.add(excelDto);
        }
        return itemDtos;
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
    public List<DeliveryReadyPiaarViewExcelItemVo> getDeliveryReadyViewOrderDataByUserId(UUID userId) {
        // 매핑데이터 조회
        List<DeliveryReadyPiaarItemViewProj> itemViewProjs = deliveryReadyPiaarService.findMappingDataByPiaarOptionCodeAndUser(userId);
        List<DeliveryReadyPiaarItemViewResDto> resDtos = itemViewProjs.stream().map(r -> DeliveryReadyPiaarItemViewResDto.toResDto(r)).collect(Collectors.toList());

        // 옵션재고수량 추가
        List<DeliveryReadyPiaarItemViewResDto> allPiaarViewItemDtos = this.getOptionStockUnit(resDtos);
        
        // ViewVo로 변경
        List<DeliveryReadyPiaarViewExcelItemVo> viewDtos = allPiaarViewItemDtos.stream().map(r -> DeliveryReadyPiaarViewExcelItemVo.toViewVo(r)).collect(Collectors.toList());
        
        return viewDtos;
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
    public List<DeliveryReadyPiaarItemViewResDto> getOptionStockUnit(List<DeliveryReadyPiaarItemViewResDto> resDtos) {
        List<String> optionCodes = resDtos.stream().map(r -> r.getDeliveryReadyItem().getOptionCode()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByProductListOptionCode(optionCodes);
    
        // 옵션 재고수량을 StockSumUnit(총 입고 수량 - 총 출고 수량)으로 변경
        List<DeliveryReadyPiaarItemViewResDto>  itemDtos = resDtos.stream().map(resDto -> {
            // 옵션 코드와 동일한 상품의 재고수량을 변경한다
            optionGetDtos.stream().forEach(option -> {
                if(resDto.getDeliveryReadyItem().getOptionCode().equals(option.getCode())) {
                    resDto.setOptionStockUnit(option.getStockSumUnit());
                }
            });
            return resDto;
        }).collect(Collectors.toList());

        return itemDtos;
    }

    public void updateListToSold(List<DeliveryReadyPiaarItemDto> itemDtos) {
        List<UUID> itemIdList = itemDtos.stream().map(dto -> dto.getId()).collect(Collectors.toList());     
        List<DeliveryReadyPiaarItemEntity> entities = deliveryReadyPiaarService.searchDeliveryReadyItemList(itemIdList);
        LocalDateTime currentTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        
        entities.forEach(entity -> {
            entity.setSoldYn("y").setSoldAt(currentTime);
        });

        deliveryReadyPiaarService.saveItemList(entities);
    }

    public void updateListToReleased(List<DeliveryReadyPiaarItemDto> itemDtos) {
        List<UUID> itemIdList = itemDtos.stream().map(dto -> dto.getId()).collect(Collectors.toList());     
        List<DeliveryReadyPiaarItemEntity> entities = deliveryReadyPiaarService.searchDeliveryReadyItemList(itemIdList);
        LocalDateTime currentTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        
        entities.forEach(entity -> {
            entity.setReleasedYn("y").setReleasedAt(currentTime);
        });

        deliveryReadyPiaarService.saveItemList(entities);
    }

    public List<PiaarCombinedDeliveryExcelItemVo> getCombinedDelivery(List<DeliveryReadyPiaarItemDto> itemDtos) {
        List<PiaarCombinedDeliveryExcelItemVo> combinedDelivery = new ArrayList<>();
        // List<DeliveryReadyPiaarItemEntity> entities = deliveryReadyPiaarService.getReleasedItemList();
        List<DeliveryReadyPiaarItemEntity> entities = itemDtos.stream().map(r -> DeliveryReadyPiaarItemEntity.toEntity(r)).collect(Collectors.toList());
        List<DeliveryReadyPiaarItemDto> dtos = entities.stream().map(r -> DeliveryReadyPiaarItemDto.toDto(r)).collect(Collectors.toList());

        Set<String> deliverySet = new HashSet<>();        // 수취인 전화번호 주소

        // 수취인 > 전화번호 > 주소 로 정렬하자
        dtos.sort(Comparator.comparing(DeliveryReadyPiaarItemDto::getReceiver)
                .thenComparing(DeliveryReadyPiaarItemDto::getReceiverContact1)
                .thenComparing(DeliveryReadyPiaarItemDto::getDestination));

        // 1. 수취인, 전화번호, 주소 동일한 값들을 itemVos에 추가
        List<DeliveryReadyPiaarItemDto> newReleasedList = new ArrayList<>();
        // 
        boolean flag = false;
        boolean flag2 = false;

        for (int i = 0; i < dtos.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(dtos.get(i).getReceiver());
            sb.append(dtos.get(i).getReceiverContact1());
            sb.append(dtos.get(i).getDestination());

            String resultStr = sb.toString();
            
            if (!deliverySet.add(resultStr)) {
                newReleasedList.add(dtos.get(i));
                
                PiaarCombinedDeliveryDetailVo detailVo = PiaarCombinedDeliveryDetailVo.builder().details(newReleasedList).build();
                PiaarCombinedDeliveryExcelItemVo dataDto = PiaarCombinedDeliveryExcelItemVo.builder().id(UUID.randomUUID()).combinedDelivery(detailVo).build();
                combinedDelivery.add(dataDto);
                
                if(flag2) {
                    if(!flag) {
                        combinedDelivery.remove(combinedDelivery.size()-2);
                    }
                    combinedDelivery.get(combinedDelivery.size()-1).setCombinedDelivery(detailVo);
                }else{
                    newReleasedList = new ArrayList<>();
                }
                
                flag = false;
                flag2 = true;
            } else {
                if(flag) {
                    PiaarCombinedDeliveryDetailVo detailVo = PiaarCombinedDeliveryDetailVo.builder().details(newReleasedList).build();
                    PiaarCombinedDeliveryExcelItemVo dataDto = PiaarCombinedDeliveryExcelItemVo.builder().id(UUID.randomUUID()).combinedDelivery(detailVo).build();
                    combinedDelivery.add(dataDto);
                    newReleasedList = new ArrayList<>();
                    flag2 = false;
                }
                newReleasedList.add(dtos.get(i));
                flag = true;
            }
        }

        // TODO :: vo형태로 return 하도록 변경. 상품명, 옵션명 등 매핑하기
        return combinedDelivery;
    }
}
