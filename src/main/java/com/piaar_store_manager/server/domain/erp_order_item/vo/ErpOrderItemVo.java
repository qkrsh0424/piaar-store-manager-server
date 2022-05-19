package com.piaar_store_manager.server.domain.erp_order_item.vo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.piaar_store_manager.server.domain.erp_order_item.dto.ErpOrderItemDto;
import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.*;

@Builder
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErpOrderItemVo {
    private UUID id;
    private String uniqueCode; // 피아르 고유코드
    private String prodName; // 상품명 / 필수값
    private String optionName; // 옵션정보 / 필수값
    private String unit; // 수량 / 필수값
    private String receiver; // 수취인명 / 필수값
    private String receiverContact1; // 전화번호1 / 필수값
    private String receiverContact2; // 전화번호2
    private String destination; // 주소 / 필수값
    private String salesChannel; // 판매채널
    private String orderNumber1; // 판매채널 주문번호1
    private String orderNumber2; // 판매채널 주문번호2
    private String channelProdCode; // 판매채널 상품코드
    private String channelOptionCode; // 판매채널 옵션코드
    private String zipCode; // 우편번호
    private String courier; // 택배사
    private String transportType; // 배송방식
    private String deliveryMessage; // 배송메세지
    private String waybillNumber;   // 운송장번호
    private String price;  // 판매금액
    private String deliveryCharge;  // 배송비
    private String barcode; // 바코드
    private String prodCode; // 피아르 상품코드
    private String optionCode; // 피아르 옵션코드
    private String releaseOptionCode;   // 출고 옵션코드
    private String managementMemo1; // 관리메모1
    private String managementMemo2; // 관리메모2
    private String managementMemo3; // 관리메모3
    private String managementMemo4; // 관리메모4
    private String managementMemo5; // 관리메모5
    private String managementMemo6; // 관리메모6
    private String managementMemo7; // 관리메모7
    private String managementMemo8; // 관리메모8
    private String managementMemo9; // 관리메모9
    private String managementMemo10; // 관리메모10
    private String freightCode; // 운송코드

    private String salesYn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime salesAt;

    private String releaseYn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime releaseAt;

    private String stockReflectYn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    private UUID createdBy;

    private String categoryName;
    private String prodDefaultName;
    private String prodManagementName;
    private String optionDefaultName;
    private String optionManagementName;
    private String optionStockUnit;

    public static ErpOrderItemVo toVo(ErpOrderItemProj proj) {
        if (proj == null)
            return null;

        ErpOrderItemVo itemVo = ErpOrderItemVo.builder()
                .id(proj.getErpOrderItem().getId())
                .uniqueCode(proj.getErpOrderItem().getUniqueCode())
                .prodName(proj.getErpOrderItem().getProdName())
                .optionName(proj.getErpOrderItem().getOptionName())
                .unit(proj.getErpOrderItem().getUnit().toString())
                .receiver(proj.getErpOrderItem().getReceiver())
                .receiverContact1(proj.getErpOrderItem().getReceiverContact1())
                .receiverContact2(proj.getErpOrderItem().getReceiverContact2())
                .destination(proj.getErpOrderItem().getDestination())
                .salesChannel(proj.getErpOrderItem().getSalesChannel())
                .orderNumber1(proj.getErpOrderItem().getOrderNumber1())
                .orderNumber2(proj.getErpOrderItem().getOrderNumber2())
                .channelProdCode(proj.getErpOrderItem().getChannelProdCode())
                .channelOptionCode(proj.getErpOrderItem().getChannelOptionCode())
                .zipCode(proj.getErpOrderItem().getZipCode())
                .courier(proj.getErpOrderItem().getCourier())
                .transportType(proj.getErpOrderItem().getTransportType())
                .deliveryMessage(proj.getErpOrderItem().getDeliveryMessage())
                .waybillNumber(proj.getErpOrderItem().getWaybillNumber())
                .price(proj.getErpOrderItem().getPrice() != null ? proj.getErpOrderItem().getPrice().toString() : null)
                .deliveryCharge(proj.getErpOrderItem().getDeliveryCharge() != null ? proj.getErpOrderItem().getDeliveryCharge().toString() : null)
                .barcode(proj.getErpOrderItem().getBarcode())
                .prodCode(proj.getErpOrderItem().getProdCode())
                .optionCode(proj.getErpOrderItem().getOptionCode())
                .releaseOptionCode(proj.getErpOrderItem().getReleaseOptionCode())
                .managementMemo1(proj.getErpOrderItem().getManagementMemo1())
                .managementMemo2(proj.getErpOrderItem().getManagementMemo2())
                .managementMemo3(proj.getErpOrderItem().getManagementMemo3())
                .managementMemo4(proj.getErpOrderItem().getManagementMemo4())
                .managementMemo5(proj.getErpOrderItem().getManagementMemo5())
                .managementMemo6(proj.getErpOrderItem().getManagementMemo6())
                .managementMemo7(proj.getErpOrderItem().getManagementMemo7())
                .managementMemo8(proj.getErpOrderItem().getManagementMemo8())
                .managementMemo9(proj.getErpOrderItem().getManagementMemo9())
                .managementMemo10(proj.getErpOrderItem().getManagementMemo10())
                .freightCode(proj.getErpOrderItem().getFreightCode())
                .salesYn(proj.getErpOrderItem().getSalesYn())
                .salesAt(proj.getErpOrderItem().getSalesAt())
                .releaseYn(proj.getErpOrderItem().getReleaseYn())
                .releaseAt(proj.getErpOrderItem().getReleaseAt())
                .stockReflectYn(proj.getErpOrderItem().getStockReflectYn())
                .createdAt(proj.getErpOrderItem().getCreatedAt())
                .createdBy(proj.getErpOrderItem().getCreatedBy())
                .categoryName(proj.getProductCategory() != null ? proj.getProductCategory().getName() : "")
                .prodDefaultName(proj.getProduct() != null ? proj.getProduct().getDefaultName() : "")
                .prodManagementName(proj.getProduct() != null ? proj.getProduct().getManagementName() : "")
                .optionDefaultName(proj.getProductOption() != null ? proj.getProductOption().getDefaultName() : "")
                .optionManagementName(proj.getProductOption() != null ? proj.getProductOption().getManagementName() : "")
                .build();

        return itemVo;
    }

    public static ErpOrderItemVo toVo(ErpOrderItemDto dto) {
        if (dto == null) return null;

        ErpOrderItemVo itemVo = ErpOrderItemVo.builder()
                .id(dto.getId())
                .uniqueCode(dto.getUniqueCode() != null ? dto.getUniqueCode().toString() : null)
                .prodName(dto.getProdName())
                .optionName(dto.getOptionName())
                .unit(dto.getUnit() != null ? dto.getUnit().toString() : null)
                .receiver(dto.getReceiver())
                .receiverContact1(dto.getReceiverContact1())
                .receiverContact2(dto.getReceiverContact2())
                .destination(dto.getDestination())
                .salesChannel(dto.getSalesChannel())
                .orderNumber1(dto.getOrderNumber1())
                .orderNumber2(dto.getOrderNumber2())
                .channelProdCode(dto.getChannelProdCode())
                .channelOptionCode(dto.getChannelOptionCode())
                .zipCode(dto.getZipCode())
                .courier(dto.getCourier())
                .transportType(dto.getTransportType())
                .deliveryMessage(dto.getDeliveryMessage())
                .waybillNumber(dto.getWaybillNumber())
                .price(dto.getPrice() != null ? dto.getPrice().toString() : null)
                .deliveryCharge(dto.getDeliveryCharge() != null ? dto.getDeliveryCharge().toString() : null)
                .barcode(dto.getBarcode())
                .prodCode(dto.getProdCode())
                .optionCode(dto.getOptionCode())
                .releaseOptionCode(dto.getReleaseOptionCode())
                .managementMemo1(dto.getManagementMemo1())
                .managementMemo2(dto.getManagementMemo2())
                .managementMemo3(dto.getManagementMemo3())
                .managementMemo4(dto.getManagementMemo4())
                .managementMemo5(dto.getManagementMemo5())
                .managementMemo6(dto.getManagementMemo6())
                .managementMemo7(dto.getManagementMemo7())
                .managementMemo8(dto.getManagementMemo8())
                .managementMemo9(dto.getManagementMemo9())
                .managementMemo10(dto.getManagementMemo10())
                .freightCode(dto.getFreightCode())
                .salesYn(dto.getSalesYn())
                .salesAt(dto.getSalesAt())
                .releaseYn(dto.getReleaseYn())
                .releaseAt(dto.getReleaseAt())
                .stockReflectYn(dto.getStockReflectYn())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .build();

        return itemVo;
    }

    public static void setOptionStockUnitForList(List<ErpOrderItemVo> erpOrderItemVos, List<ProductOptionEntity> optionEntities) {
        erpOrderItemVos.forEach(erpOrderItemVo -> {
            optionEntities.forEach(optionEntity ->{
                if(!erpOrderItemVo.getOptionCode().isEmpty() && erpOrderItemVo.getOptionCode().equals(optionEntity.getCode())){
                    erpOrderItemVo.setOptionStockUnit(optionEntity.getStockSumUnit().toString());
                    return;
                }
            });
        });
    }

    public static List<ErpOrderItemVo> excelSheetToVos(Sheet worksheet) {
        Integer PIAAR_ERP_ORDER_ITEM_SIZE = 34;
        Integer PIAAR_ERP_ORDER_MEMO_START_INDEX = 24;

        List<Integer> PIAAR_ERP_ORDER_REQUIRED_HEADER_INDEX = Arrays.asList(1, 2, 3, 4, 5, 7);

        // List<String> PIAAR_ERP_ORDER_HEADER_NAME_LIST = Arrays.asList(
        //         "피아르 고유번호",
        //         "상품명",
        //         "옵션정보",
        //         "수량",
        //         "수취인명",
        //         "전화번호1",
        //         "전화번호2",
        //         "주소",
        //         "판매채널",
        //         "판매채널 주문번호1",
        //         "판매채널 주문번호2",
        //         "판매채널 상품코드",
        //         "판매채널 옵션코드",
        //         "우편번호",
        //         "택배사",
        //         "배송방식",
        //         "배송메세지",
        //         "운송장번호",
        //         "판매금액",
        //         "배송비",
        //         "바코드",
        //         "피아르 상품코드",
        //         "피아르 옵션코드",
        //         "출고 옵션코드",
        //         "관리메모1",
        //         "관리메모2",
        //         "관리메모3",
        //         "관리메모4",
        //         "관리메모5",
        //         "관리메모6",
        //         "관리메모7",
        //         "관리메모8",
        //         "관리메모9",
        //         "관리메모10"
        // );

        List<ErpOrderItemVo> itemVos = new ArrayList<>();
        
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if (row == null) break;

            Object cellValue = new Object();
            List<String> customManagementMemo = new ArrayList<>();

            // type check and data setting of managementMemo1~10.
            for (int j = PIAAR_ERP_ORDER_MEMO_START_INDEX; j < PIAAR_ERP_ORDER_ITEM_SIZE; j++) {
                Cell cell = row.getCell(j);

                if (cell == null || cell.getCellType().equals(CellType.BLANK)) {
                    cellValue = "";
                } else if (cell.getCellType().equals(CellType.NUMERIC)) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Instant instant = Instant.ofEpochMilli(cell.getDateCellValue().getTime());
                        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

                        // yyyy-MM-dd'T'HH:mm:ss -> yyyy-MM-dd HH:mm:ss로 변경
                        cellValue = CustomDateUtils.getLocalDateTimeToyyyyMMddHHmmss(localDateTime);
                    } else {
                        cellValue = cell.getNumericCellValue();
                    }
                } else {
                    cellValue = cell.getStringCellValue();
                }
                customManagementMemo.add(cellValue.toString());
            }

            // 피아르 양식 필수값 검사
            for(int j = 0; j < PIAAR_ERP_ORDER_REQUIRED_HEADER_INDEX.size(); j++) {
                Integer requiredHeaderIdx = PIAAR_ERP_ORDER_REQUIRED_HEADER_INDEX.get(j);
                if(row.getCell(requiredHeaderIdx) == null || row.getCell(requiredHeaderIdx).getCellType().equals(CellType.BLANK)) {
                    throw new CustomInvalidDataException("필수값 항목이 비어있습니다. 수정 후 재업로드 해주세요.");
                }
            }

            // price, deliveryCharge - 엑셀 타입 string, number 허용
            String priceStr = (row.getCell(18) == null) ? "0" : (row.getCell(18).getCellType().equals(CellType.NUMERIC) ?
                    Integer.toString((int) row.getCell(18).getNumericCellValue()) : row.getCell(18).getStringCellValue());

            String deliveryChargeStr = (row.getCell(19) == null) ? "0" : (row.getCell(19).getCellType().equals(CellType.NUMERIC) ?
                    Integer.toString((int) row.getCell(19).getNumericCellValue()) : row.getCell(19).getStringCellValue());

            // '출고 옵션코드' 값이 입력되지 않았다면 '피아르 옵션코드'로 대체한다
            String releaseOptionCode = (row.getCell(23) != null) ? row.getCell(23).getStringCellValue() : (row.getCell(22) == null ? "" : row.getCell(22).getStringCellValue());          

            ErpOrderItemVo excelVo = ErpOrderItemVo.builder()
                    .uniqueCode(null)
                    .prodName(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "")
                    .optionName(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "")
                    .unit(row.getCell(3) != null ? Integer.toString((int) row.getCell(3).getNumericCellValue()) : "")
                    .receiver(row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "")
                    .receiverContact1(row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "")
                    .receiverContact2(row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "")
                    .destination(row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "")
                    .salesChannel(row.getCell(8) != null ? row.getCell(8).getStringCellValue() : "")
                    .orderNumber1(row.getCell(9) != null ? row.getCell(9).getStringCellValue() : "")
                    .orderNumber2(row.getCell(10) != null ? row.getCell(10).getStringCellValue() : "")
                    .channelProdCode(row.getCell(11) != null ? row.getCell(11).getStringCellValue() : "")
                    .channelOptionCode(row.getCell(12) != null ? row.getCell(12).getStringCellValue() : "")
                    .zipCode(row.getCell(13) != null ? row.getCell(13).getStringCellValue() : "")
                    .courier(row.getCell(14) != null ? row.getCell(14).getStringCellValue() : "")
                    .transportType(row.getCell(15) != null ? row.getCell(15).getStringCellValue() : "")
                    .deliveryMessage(row.getCell(16) != null ? row.getCell(16).getStringCellValue() : "")
                    .waybillNumber(row.getCell(17) != null ? row.getCell(17).getStringCellValue() : "")
                    .price(priceStr)
                    .deliveryCharge(deliveryChargeStr)
                    .barcode(row.getCell(20) != null ? row.getCell(20).getStringCellValue() : "")
                    .prodCode(row.getCell(21) != null ? row.getCell(21).getStringCellValue() : "")
                    .optionCode(row.getCell(22) != null ? row.getCell(22).getStringCellValue() : "")
                    .releaseOptionCode(releaseOptionCode)
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
                    .freightCode(null)
                    .build();

            itemVos.add(excelVo);
        }
        return itemVos;
    }
}
