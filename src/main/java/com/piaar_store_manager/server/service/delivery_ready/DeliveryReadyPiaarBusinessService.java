package com.piaar_store_manager.server.service.delivery_ready;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.piaar_store_manager.server.exception.AccessDeniedException;
import com.piaar_store_manager.server.exception.FileUploadException;
import com.piaar_store_manager.server.exception.InvalidUserException;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemDto;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemUnitCombinedDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.delivery_ready.piaar.entity.DeliveryReadyPiaarItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.piaar.proj.DeliveryReadyPiaarItemViewProj;
import com.piaar_store_manager.server.model.delivery_ready.piaar.vo.DeliveryReadyPiaarItemVo;
import com.piaar_store_manager.server.model.delivery_ready.piaar.vo.PiaarCombinedDeliveryItemVo;
import com.piaar_store_manager.server.model.delivery_ready.piaar.vo.PiaarUnitCombinedDeliveryItemVo;
import com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.dto.DeliveryReadyPiaarViewHeaderDto;
import com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.entity.DeliveryReadyPiaarViewHeaderEntity;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.service.delivery_ready_view_header.DeliveryReadyPiaarViewHeaderService;
import com.piaar_store_manager.server.service.product_option.ProductOptionService;
import com.piaar_store_manager.server.service.user.UserService;
import com.piaar_store_manager.server.utils.CustomFieldUtils;

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
    private UserService userService;
    private DeliveryReadyPiaarViewHeaderService deliveryReadyPiaarViewHeaderService;

    @Autowired
    public DeliveryReadyPiaarBusinessService(
            DeliveryReadyPiaarService deliveryReadyPiaarService,
            ProductOptionService productOptionService,
            UserService userService,
            DeliveryReadyPiaarViewHeaderService deliveryReadyPiaarViewHeaderService) {
        this.deliveryReadyPiaarService = deliveryReadyPiaarService;
        this.productOptionService = productOptionService;
        this.userService = userService;
        this.deliveryReadyPiaarViewHeaderService = deliveryReadyPiaarViewHeaderService;
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

        if (EXTENSIONS_EXCEL.contains(extension)) {
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
    public List<DeliveryReadyPiaarItemVo> uploadDeliveryReadyExcelFile(MultipartFile file) {
        if (!userService.isUserLogin()) {
            throw new InvalidUserException("로그인이 필요한 서비스 입니다.");
        }

        if (!userService.isManager()) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }

        // TODO : 타입체크 메서드 구현해야됨.
        Sheet sheet = workbook.getSheetAt(0);

        List<DeliveryReadyPiaarItemVo> vos = this.getDeliveryReadyExcelForm(sheet);
        return vos;
    }

    private List<DeliveryReadyPiaarItemVo> getDeliveryReadyExcelForm(Sheet worksheet) {
        List<DeliveryReadyPiaarItemVo> itemVos = new ArrayList<>();

        Row firstRow = worksheet.getRow(0);
        // 피아르 엑셀 양식 검사
        for (int i = 0; i < PIAAR_EXCEL_HEADER_SIZE; i++) {
            Cell cell = firstRow.getCell(i);
            String headerName = cell != null ? cell.getStringCellValue() : null;
            // 지정된 양식이 아니라면
            if (!PIAAR_EXCEL_HEADER_LIST.get(i).equals(headerName)) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if (row == null)
                break;

            Object cellValue = new Object();
            List<String> customManagementMemo = new ArrayList<>();

            // type check and data setting of managementMemo1~20.
            for (int j = PIAAR_MEMO_START_INDEX; j < PIAAR_EXCEL_HEADER_SIZE; j++) {
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

            DeliveryReadyPiaarItemVo excelVo = DeliveryReadyPiaarItemVo.builder()
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

            itemVos.add(excelVo);
        }
        return itemVos;
    }

    public void createItemList(List<DeliveryReadyPiaarItemDto> deliveryReadyPiaarItemDtos) {
        if (!userService.isUserLogin()) {
            throw new InvalidUserException("로그인이 필요한 서비스 입니다.");
        }

        if (!userService.isManager()) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        UUID USER_ID = userService.getUserId();

        List<DeliveryReadyPiaarItemEntity> deliveryReadyPiaarItemEntities = deliveryReadyPiaarItemDtos.stream()
                .map(r -> {
                    r.setId(UUID.randomUUID())
                            .setUniqueCode(UUID.randomUUID())
                            .setSoldYn("n")
                            .setReleasedYn("n")
                            .setStockReflectedYn("n")
                            .setCreatedAt(DateHandler.getCurrentLocalDateTime())
                            .setCreatedBy(USER_ID);

                    return DeliveryReadyPiaarItemEntity.toEntity(r);
                }).collect(Collectors.toList());

        deliveryReadyPiaarService.saveItemList(deliveryReadyPiaarItemEntities);
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
    public List<DeliveryReadyPiaarItemVo> getDeliveryReadyViewOrderDataByUserId() {
        if (!userService.isUserLogin()) {
            throw new InvalidUserException("로그인이 필요한 서비스 입니다.");
        }

        if (!userService.isManager()) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
        
        // 매핑데이터 조회
        List<DeliveryReadyPiaarItemViewProj> itemViewProjs = deliveryReadyPiaarService.findMappingDataByPiaarOptionCodeAndUser(userService.getUserId());
        List<DeliveryReadyPiaarItemVo> itemVos = itemViewProjs.stream().map(r -> DeliveryReadyPiaarItemVo.toVo(r)).collect(Collectors.toList());

        // 옵션재고수량 추가
        List<DeliveryReadyPiaarItemVo> deliveryReadyPiaarItemVos = this.getOptionStockUnit(itemVos);
        return deliveryReadyPiaarItemVos;
    }

    public List<DeliveryReadyPiaarItemVo> getOptionStockUnit(List<DeliveryReadyPiaarItemVo> itemVos) {
        List<String> optionCodes = itemVos.stream().map(r -> r.getOptionCode()).collect(Collectors.toList());
        List<ProductOptionGetDto> optionGetDtos = productOptionService.searchListByProductListOptionCode(optionCodes);

        // 옵션 재고수량을 StockSumUnit(총 입고 수량 - 총 출고 수량)으로 변경
        List<DeliveryReadyPiaarItemVo> deliveryReadyPiaarItemVos = itemVos.stream().map(itemVo -> {
            // 옵션 코드와 동일한 상품의 재고수량을 변경한다
            optionGetDtos.stream().forEach(option -> {
                if (itemVo.getOptionCode().equals(option.getCode())) {
                    itemVo.setOptionStockUnit(option.getStockSumUnit());
                }
            });
            return itemVo;
        }).collect(Collectors.toList());

        return deliveryReadyPiaarItemVos;
    }

    public void updateListToSold(List<DeliveryReadyPiaarItemDto> itemDtos) {
        if (!userService.isUserLogin()) {
            throw new InvalidUserException("로그인이 필요한 서비스 입니다.");
        }

        if (!userService.isManager()) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        List<UUID> itemIdList = itemDtos.stream().map(dto -> dto.getId()).collect(Collectors.toList());
        List<DeliveryReadyPiaarItemEntity> entities = deliveryReadyPiaarService.searchDeliveryReadyItemList(itemIdList);

        entities.forEach(entity -> {
            entity.setSoldYn("y").setSoldAt(DateHandler.getCurrentLocalDateTime());
        });

        deliveryReadyPiaarService.saveItemList(entities);
    }

    public void updateListToReleased(List<DeliveryReadyPiaarItemDto> itemDtos) {
        if (!userService.isUserLogin()) {
            throw new InvalidUserException("로그인이 필요한 서비스 입니다.");
        }

        if (!userService.isManager()) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
        
        List<UUID> itemIdList = itemDtos.stream().map(dto -> dto.getId()).collect(Collectors.toList());
        List<DeliveryReadyPiaarItemEntity> entities = deliveryReadyPiaarService.searchDeliveryReadyItemList(itemIdList);

        entities.forEach(entity -> {
            entity.setReleasedYn("y").setReleasedAt(DateHandler.getCurrentLocalDateTime());
        });

        deliveryReadyPiaarService.saveItemList(entities);
    }

    // 수취인 + 전화번호 + 주소
    public List<PiaarCombinedDeliveryItemVo> getCombinedDelivery(List<DeliveryReadyPiaarItemDto> dtos) {
        if (!userService.isUserLogin()) {
            throw new InvalidUserException("로그인이 필요한 서비스 입니다.");
        }

        if (!userService.isManager()) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
        
        List<PiaarCombinedDeliveryItemVo> combinedDelivery = new ArrayList<>();
        Set<String> deliverySet = new HashSet<>(); // 수취인 전화번호 주소

        // 수취인 > 전화번호 > 주소 > 상품명 > 옵션명 으로 정렬
        dtos.sort(Comparator.comparing(DeliveryReadyPiaarItemDto::getReceiver)
                .thenComparing(DeliveryReadyPiaarItemDto::getReceiverContact1)
                .thenComparing(DeliveryReadyPiaarItemDto::getDestination)
                .thenComparing(DeliveryReadyPiaarItemDto::getProdName)
                .thenComparing(DeliveryReadyPiaarItemDto::getOptionName));

        for (int i = 0; i < dtos.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(dtos.get(i).getReceiver());
            sb.append(dtos.get(i).getReceiverContact1());
            sb.append(dtos.get(i).getDestination());

            String resultStr = sb.toString();
            List<DeliveryReadyPiaarItemDto> newCombinedList = new ArrayList<>();
            PiaarCombinedDeliveryItemVo itemVo = new PiaarCombinedDeliveryItemVo();

            // 새로운 데이터라면
            if (deliverySet.add(resultStr)) {
                newCombinedList.add(dtos.get(i));
                itemVo = PiaarCombinedDeliveryItemVo.builder().combinedDeliveryItems(newCombinedList).build(); 
                combinedDelivery.add(itemVo);
            } else { // 중복된다면
                // 이전 데이터에 현재 데이터를 추가한다
                newCombinedList = combinedDelivery.get(combinedDelivery.size() - 1).getCombinedDeliveryItems();
                newCombinedList.add(dtos.get(i));
                
                itemVo = PiaarCombinedDeliveryItemVo.builder().combinedDeliveryItems(newCombinedList).build();

                // 이전 결합배송 리스트를 수정한다
                combinedDelivery.set(combinedDelivery.size()-1, itemVo);
            }
        }

        return combinedDelivery;
    }

    // 수취인 + 전화번호 + 주소 + 상품명 + 옵션명 => 수량+
    public List<PiaarUnitCombinedDeliveryItemVo> getUnitCombinedDelivery(List<DeliveryReadyPiaarItemDto> dtos) {
        // 기본 합배송 설정된 데이터들 가져오기
        List<PiaarCombinedDeliveryItemVo> combinedDelivery= this.getCombinedDelivery(dtos);
        
        // 기본 합배송 vo에서 수량 및 병합데이터 vo로 변경
        List<PiaarUnitCombinedDeliveryItemVo> unitCombinedDelivery = combinedDelivery.stream().map(r -> {
            List<DeliveryReadyPiaarItemUnitCombinedDto> combinedDtos = r.getCombinedDeliveryItems().stream().map(itemDto -> DeliveryReadyPiaarItemUnitCombinedDto.toUnitCombinedDto(itemDto)).collect(Collectors.toList());
            PiaarUnitCombinedDeliveryItemVo vo = PiaarUnitCombinedDeliveryItemVo.builder().combinedDeliveryItems(combinedDtos).build();
            return vo;
        }).collect(Collectors.toList());

        DeliveryReadyPiaarViewHeaderEntity viewHeaderEntity = deliveryReadyPiaarViewHeaderService.searchOneByUser(userService.getUserId());
        DeliveryReadyPiaarViewHeaderDto viewHeaderDto = DeliveryReadyPiaarViewHeaderDto.toDto(viewHeaderEntity);
        List<String> combinedColumnName = viewHeaderDto.getViewHeaderDetail().getDetails().stream().filter(r -> r.getMergeYn().equals("y")).collect(Collectors.toList()).stream()
            .map(r -> r.getMatchedColumnName()).collect(Collectors.toList());

        Set<String> deliverySet = new HashSet<>(); // 수취인 전화번호 주소

        for (int i = 0; i < unitCombinedDelivery.size(); i++) {
            // 주문자 정보로 묶여진 데이터들끼리 비교
            for (int j = 0; j < unitCombinedDelivery.get(i).getCombinedDeliveryItems().size(); j++) {
                DeliveryReadyPiaarItemUnitCombinedDto currentDto = unitCombinedDelivery.get(i).getCombinedDeliveryItems().get(j);
                
                StringBuilder sb = new StringBuilder();
                sb.append(currentDto.getReceiverContact1());
                sb.append(currentDto.getReceiver());
                sb.append(currentDto.getDestination());
                sb.append(currentDto.getProdName());
                sb.append(currentDto.getOptionName());

                String resultStr = sb.toString();

                if((!deliverySet.add(resultStr)) && (j > 0)) {
                    DeliveryReadyPiaarItemUnitCombinedDto prevDto = unitCombinedDelivery.get(i).getCombinedDeliveryItems().get(j-1);
                    // 중복 (상품+옵션) 수량 더하기
                    CustomFieldUtils.setFieldValue(prevDto, "unit", prevDto.getUnit() + currentDto.getUnit());

                    // mergeYn이 y인 데이터들을 한 컬럼에 나열
                    combinedColumnName.forEach(columnName -> {
                        String prevFieldValue = CustomFieldUtils.getFieldValue(prevDto, columnName) == null ? "" : CustomFieldUtils.getFieldValue(prevDto, columnName);
                        String currentFieldValue = CustomFieldUtils.getFieldValue(prevDto, columnName) == null ? "" : CustomFieldUtils.getFieldValue(prevDto, columnName);
                        
                        if (!columnName.equals("unit")) {
                            CustomFieldUtils.setFieldValue(prevDto, columnName, prevFieldValue + "|&&|" + currentFieldValue);
                        }
                    });
                    
                    // 중복 데이터 제거
                    unitCombinedDelivery.get(i).getCombinedDeliveryItems().remove(j);
                }
            }
        }

        return unitCombinedDelivery;
    }
}
