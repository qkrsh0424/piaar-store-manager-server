package com.piaar_store_manager.server.domain.erp_order_item.vo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.piaar_store_manager.server.domain.erp_order_item.dto.ErpOrderItemDto;
import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;

import com.piaar_store_manager.server.utils.CustomExcelUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    private String destinationDetail; // 주소 상세
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime channelOrderDate; // 채널 주문일시
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
    private String returnYn;
    // private String exchangeYn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    private UUID createdBy;

    private String categoryName;
    private String prodDefaultName;
    private String prodManagementName;
    private String optionDefaultName;
    private String optionManagementName;
    private String optionStockUnit;
    private String optionReleaseLocation;
    
    // 221125 생성
    private String optionPackageYn;

    @Builder
    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExcelVo{
        private UUID id;
        private Object uniqueCode; // 피아르 고유코드
        private Object prodName; // 상품명 / 필수값
        private Object optionName; // 옵션정보 / 필수값
        private Object unit; // 수량 / 필수값
        private Object receiver; // 수취인명 / 필수값
        private Object receiverContact1; // 전화번호1 / 필수값
        private Object receiverContact2; // 전화번호2
        private Object destination; // 주소 / 필수값
        private Object destinationDetail; // 주소 상세
        private Object salesChannel; // 판매채널
        private Object orderNumber1; // 판매채널 주문번호1
        private Object orderNumber2; // 판매채널 주문번호2
        private Object channelProdCode; // 판매채널 상품코드
        private Object channelOptionCode; // 판매채널 옵션코드
        private Object zipCode; // 우편번호
        private Object courier; // 택배사
        private Object transportType; // 배송방식
        private Object deliveryMessage; // 배송메세지
        private Object waybillNumber;   // 운송장번호
        private Object price;  // 판매금액
        private Object deliveryCharge;  // 배송비
        private Object barcode; // 바코드
        private Object prodCode; // 피아르 상품코드
        private Object optionCode; // 피아르 옵션코드
        private Object releaseOptionCode;   // 출고 옵션코드
        private Object channelOrderDate; // 채널 주문일시
        private Object managementMemo1; // 관리메모1
        private Object managementMemo2; // 관리메모2
        private Object managementMemo3; // 관리메모3
        private Object managementMemo4; // 관리메모4
        private Object managementMemo5; // 관리메모5
        private Object managementMemo6; // 관리메모6
        private Object managementMemo7; // 관리메모7
        private Object managementMemo8; // 관리메모8
        private Object managementMemo9; // 관리메모9
        private Object managementMemo10; // 관리메모10
        private Object freightCode; // 운송코드

        private Object salesYn;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime salesAt;

        private Object releaseYn;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime releaseAt;

        private Object stockReflectYn;
        private Object returnYn;
        // private Object exchangeYn;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime createdAt;

        private UUID createdBy;
    }

    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManyToOneJoin {
        ErpOrderItemVo erpOrderItem;
        ProductCategoryGetDto productCategory;
        ProductGetDto product;
        ProductOptionGetDto productOption;

        public static ManyToOneJoin toVo(ErpOrderItemProj proj) {
            ErpOrderItemVo erpOrderItem = ErpOrderItemVo.toVo(proj);
            ProductCategoryGetDto productCategory = null;
            ProductGetDto product = null;
            ProductOptionGetDto productOption = null;

            if(proj.getProductOption() != null) {
                productCategory = ProductCategoryGetDto.toDto(proj.getProductCategory());
                product = ProductGetDto.toDto(proj.getProduct());
                productOption = ProductOptionGetDto.toDto(proj.getProductOption());
            }

            ManyToOneJoin vo = ManyToOneJoin.builder()
                .erpOrderItem(erpOrderItem)
                .product(product)
                .productCategory(productCategory)
                .productOption(productOption)
                .build();

            return vo;
        }
    }


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
                .destinationDetail(proj.getErpOrderItem().getDestinationDetail())
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
                .channelOrderDate(proj.getErpOrderItem().getChannelOrderDate())
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
                .returnYn(proj.getErpOrderItem().getReturnYn())
                // .exchangeYn(proj.getErpOrderItem().getExchangeYn())
                .createdAt(proj.getErpOrderItem().getCreatedAt())
                .createdBy(proj.getErpOrderItem().getCreatedBy())
                .categoryName(proj.getProductCategory() != null ? proj.getProductCategory().getName() : "")
                .prodDefaultName(proj.getProduct() != null ? proj.getProduct().getDefaultName() : "")
                .prodManagementName(proj.getProduct() != null ? proj.getProduct().getManagementName() : "")
                .optionDefaultName(proj.getProductOption() != null ? proj.getProductOption().getDefaultName() : "")
                .optionManagementName(proj.getProductOption() != null ? proj.getProductOption().getManagementName() : "")
                .optionReleaseLocation(proj.getProductOption() != null ? proj.getProductOption().getReleaseLocation() : "")
                .optionPackageYn(proj.getProductOption() != null ? proj.getProductOption().getPackageYn() : "n")
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
                .destinationDetail(dto.getDestinationDetail())
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
                .channelOrderDate(dto.getChannelOrderDate())
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
                .returnYn(dto.getReturnYn())
                // .exchangeYn(dto.getExchangeYn())
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

    public static void setReleaseOptionStockUnitForList(List<ErpOrderItemVo> erpOrderItemVos, List<ProductOptionEntity> optionEntities) {
        erpOrderItemVos.forEach(erpOrderItemVo -> {
            optionEntities.forEach(optionEntity ->{
                if(!erpOrderItemVo.getReleaseOptionCode().isEmpty() && erpOrderItemVo.getReleaseOptionCode().equals(optionEntity.getCode())){
                    erpOrderItemVo.setOptionStockUnit(optionEntity.getStockSumUnit().toString());
                    return;
                }
            });
        });
    }

    public static void setOptionStockUnitForM2OJList(List<ErpOrderItemVo.ManyToOneJoin> erpOrderItemM2OJVos, List<ProductOptionEntity> optionEntities) {
        erpOrderItemM2OJVos.forEach(erpOrderItemM2OJVo -> {
            optionEntities.forEach(optionEntity ->{
                if(!erpOrderItemM2OJVo.getErpOrderItem().getOptionCode().isEmpty() && erpOrderItemM2OJVo.getErpOrderItem().getOptionCode().equals(optionEntity.getCode())){
                    erpOrderItemM2OJVo.getErpOrderItem().setOptionStockUnit(optionEntity.getStockSumUnit().toString());
                    return;
                }
            });
        });
    }

    public static void setReleaseOptionStockUnitForM2OJList(List<ErpOrderItemVo.ManyToOneJoin> erpOrderItemM2OJVos, List<ProductOptionEntity> optionEntities) {
        erpOrderItemM2OJVos.forEach(erpOrderItemM2OJVo -> {
            optionEntities.forEach(optionEntity ->{
                if(!erpOrderItemM2OJVo.getErpOrderItem().getReleaseOptionCode().isEmpty() && erpOrderItemM2OJVo.getErpOrderItem().getReleaseOptionCode().equals(optionEntity.getCode())){
                    erpOrderItemM2OJVo.getErpOrderItem().setOptionStockUnit(optionEntity.getStockSumUnit().toString());
                    return;
                }
            });
        });
    }

    public static List<ErpOrderItemVo.ExcelVo> excelSheetToVos(Sheet worksheet) {
        List<Integer> PIAAR_ERP_ORDER_REQUIRED_HEADER_INDEX = Arrays.asList(1, 2, 3, 4, 5, 7);

        List<ErpOrderItemVo.ExcelVo> itemVos = new ArrayList<>();

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if (row == null) break;

            // 피아르 양식 필수값 검사
            for(int j = 0; j < PIAAR_ERP_ORDER_REQUIRED_HEADER_INDEX.size(); j++) {
                Integer requiredHeaderIdx = PIAAR_ERP_ORDER_REQUIRED_HEADER_INDEX.get(j);
                if(row.getCell(requiredHeaderIdx) == null || CustomExcelUtils.isBlankCell(row.getCell(requiredHeaderIdx))) {
                        throw new CustomInvalidDataException("필수값 항목이 비어있습니다. 수정 후 재업로드 해주세요.");
                }
            }
            
            // 수량, 금액, 배송비 항목값을 Integer 타입으로 변환한다
            Object unitObj = CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(3), 0);
            Object priceObj = CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(19), 0);
            Object deliveryChargeObj = CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(20), 0);

            if(!unitObj.getClass().equals(Integer.class)) {
                unitObj = CustomExcelUtils.convertObjectValueToIntegerValue(unitObj);
            }
            if(!priceObj.getClass().equals(Integer.class)){
                priceObj = CustomExcelUtils.convertObjectValueToIntegerValue(priceObj);
            }
            if(!deliveryChargeObj.getClass().equals(Integer.class)) {
                deliveryChargeObj = CustomExcelUtils.convertObjectValueToIntegerValue(deliveryChargeObj);
            }

            ErpOrderItemVo.ExcelVo excelVo = ErpOrderItemVo.ExcelVo.builder()
                    .id(UUID.randomUUID())
                    .uniqueCode(null)
                    .prodName(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(1), ""))
                    .optionName(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(2), ""))
                    .unit(unitObj)
                    .receiver(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(4), ""))
                    .receiverContact1(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(5), ""))
                    .receiverContact2(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(6), ""))
                    .destination(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(7), ""))
                    .destinationDetail(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(8), ""))
                    .salesChannel(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(9), ""))
                    .orderNumber1(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(10), ""))
                    .orderNumber2(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(11), ""))
                    .channelProdCode(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(12), ""))
                    .channelOptionCode(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(13), ""))
                    .zipCode(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(14), ""))
                    .courier(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(15), ""))
                    .transportType(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(16), ""))
                    .deliveryMessage(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(17), ""))
                    .waybillNumber(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(18), ""))
                    .price(priceObj)
                    .deliveryCharge(deliveryChargeObj)
                    .barcode(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(21), ""))
                    .prodCode(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(22), ""))
                    .optionCode(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(23), ""))
                    .releaseOptionCode(
                            CustomExcelUtils.getCellValueObjectWithDefaultValue(
                                    row.getCell(24),
                                    CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(23), "")
                            )
                    )
                    .channelOrderDate(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(25), ""))
                    .managementMemo1(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(26), ""))
                    .managementMemo2(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(27), ""))
                    .managementMemo3(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(28), ""))
                    .managementMemo4(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(29), ""))
                    .managementMemo5(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(30), ""))
                    .managementMemo6(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(31), ""))
                    .managementMemo7(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(32), ""))
                    .managementMemo8(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(33), ""))
                    .managementMemo9(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(34), ""))
                    .managementMemo10(CustomExcelUtils.getCellValueObjectWithDefaultValue(row.getCell(35), ""))
                    .freightCode(null)
                    .build();

            itemVos.add(excelVo);
        }
        return itemVos;
    }
}
