package com.piaar_store_manager.server.model.delivery_ready.piaar.vo;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemViewResDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReadyPiaarViewExcelItemVo {
    private UUID id;
    private UUID uniqueCode;      // 피아르 고유코드
    private String orderNumber1;        // 주문번호1
    private String orderNumber2;        // 주문번호2
    private String orderNumber3;        // 주문번호3
    private String prodName;        // 상품명 / 필수값
    private String optionName;      // 옵션명 / 필수값
    private Integer unit;       // 수량 / 필수값
    private String receiver;        // 수취인명 / 필수값
    private String receiverContact1;        // 전화번호1 / 필수값
    private String receiverContact2;        // 전화번호2
    private String destination;     // 주소 / 필수값
    private String zipCode;     // 우편번호
    private String transportType;       // 배송방식
    private String deliveryMessage;     // 배송메세지
    private String prodUniqueNumber1;       // 상품고유번호1
    private String prodUniqueNumber2;       // 상품고유번호2
    private String optionUniqueNumber1;     // 옵션고유번호1
    private String optionUniqueNumber2;     // 옵션고유번호2
    private String prodCode;        // 피아르 상품코드
    private String optionCode;      // 피아르 옵션코드
    private String managementMemo1;     // 관리메모1
    private String managementMemo2;     // 관리메모2
    private String managementMemo3;     // 관리메모3
    private String managementMemo4;     // 관리메모4
    private String managementMemo5;     // 관리메모5
    private String managementMemo6;     // 관리메모6
    private String managementMemo7;     // 관리메모7
    private String managementMemo8;     // 관리메모8
    private String managementMemo9;     // 관리메모9
    private String managementMemo10;     // 관리메모10
    private String managementMemo11;     // 관리메모11
    private String managementMemo12;     // 관리메모12
    private String managementMemo13;     // 관리메모13
    private String managementMemo14;     // 관리메모14
    private String managementMemo15;     // 관리메모15
    private String managementMemo16;     // 관리메모16
    private String managementMemo17;     // 관리메모17
    private String managementMemo18;     // 관리메모18
    private String managementMemo19;     // 관리메모19
    private String managementMemo20;     // 관리메모20
    
    private String categoryName;
    private String prodDefaultName;
    private String prodManagementName;
    private String optionDefaultName;
    private String optionManagementName;
    private Integer optionStockUnit;

    private String soldYn;
    private LocalDateTime soldAt;
    private String releasedYn;
    private LocalDateTime releasedAt;
    private String stockReflectedYn;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Integer deliveryReadyFileCid;
    

    public static DeliveryReadyPiaarViewExcelItemVo toViewVo(DeliveryReadyPiaarItemViewResDto dto) {
        DeliveryReadyPiaarViewExcelItemVo viewVo = DeliveryReadyPiaarViewExcelItemVo.builder()
            .id(dto.getDeliveryReadyItem().getId())
            .uniqueCode(dto.getDeliveryReadyItem().getUniqueCode())
            .orderNumber1(dto.getDeliveryReadyItem().getOrderNumber1())
            .orderNumber2(dto.getDeliveryReadyItem().getOrderNumber2())
            .orderNumber3(dto.getDeliveryReadyItem().getOrderNumber3())
            .prodName(dto.getDeliveryReadyItem().getProdName())
            .optionName(dto.getDeliveryReadyItem().getOptionName())
            .unit(dto.getDeliveryReadyItem().getUnit())
            .receiver(dto.getDeliveryReadyItem().getReceiver())
            .receiverContact1(dto.getDeliveryReadyItem().getReceiverContact1())
            .receiverContact2(dto.getDeliveryReadyItem().getReceiverContact2())
            .destination(dto.getDeliveryReadyItem().getDestination())
            .zipCode(dto.getDeliveryReadyItem().getZipCode())
            .transportType(dto.getDeliveryReadyItem().getTransportType())
            .deliveryMessage(dto.getDeliveryReadyItem().getDeliveryMessage())
            .prodUniqueNumber1(dto.getDeliveryReadyItem().getProdUniqueNumber1())
            .prodUniqueNumber2(dto.getDeliveryReadyItem().getProdUniqueNumber2())
            .optionUniqueNumber1(dto.getDeliveryReadyItem().getOptionUniqueNumber1())
            .optionUniqueNumber2(dto.getDeliveryReadyItem().getOptionUniqueNumber2())
            .prodCode(dto.getDeliveryReadyItem().getProdCode())
            .optionCode(dto.getDeliveryReadyItem().getOptionCode())
            .managementMemo1(dto.getDeliveryReadyItem().getManagementMemo1())
            .managementMemo2(dto.getDeliveryReadyItem().getManagementMemo2())
            .managementMemo3(dto.getDeliveryReadyItem().getManagementMemo3())
            .managementMemo4(dto.getDeliveryReadyItem().getManagementMemo4())
            .managementMemo5(dto.getDeliveryReadyItem().getManagementMemo5())
            .managementMemo6(dto.getDeliveryReadyItem().getManagementMemo6())
            .managementMemo7(dto.getDeliveryReadyItem().getManagementMemo7())
            .managementMemo8(dto.getDeliveryReadyItem().getManagementMemo8())
            .managementMemo9(dto.getDeliveryReadyItem().getManagementMemo9())
            .managementMemo10(dto.getDeliveryReadyItem().getManagementMemo10())
            .managementMemo11(dto.getDeliveryReadyItem().getManagementMemo11())
            .managementMemo12(dto.getDeliveryReadyItem().getManagementMemo12())
            .managementMemo13(dto.getDeliveryReadyItem().getManagementMemo13())
            .managementMemo14(dto.getDeliveryReadyItem().getManagementMemo14())
            .managementMemo15(dto.getDeliveryReadyItem().getManagementMemo15())
            .managementMemo16(dto.getDeliveryReadyItem().getManagementMemo16())
            .managementMemo17(dto.getDeliveryReadyItem().getManagementMemo17())
            .managementMemo18(dto.getDeliveryReadyItem().getManagementMemo18())
            .managementMemo19(dto.getDeliveryReadyItem().getManagementMemo19())
            .managementMemo20(dto.getDeliveryReadyItem().getManagementMemo20())
            .categoryName(dto.getCategoryName())
            .prodDefaultName(dto.getProdDefaultName())
            .prodManagementName(dto.getProdManagementName())
            .optionDefaultName(dto.getOptionDefaultName())
            .optionManagementName(dto.getOptionManagementName())
            .optionStockUnit(dto.getOptionStockUnit())
            .soldYn(dto.getDeliveryReadyItem().getSoldYn())
            .soldAt(dto.getDeliveryReadyItem().getSoldAt())
            .releasedYn(dto.getDeliveryReadyItem().getReleasedYn())
            .releasedAt(dto.getDeliveryReadyItem().getReleasedAt())
            .stockReflectedYn(dto.getDeliveryReadyItem().getStockReflectedYn())
            .createdAt(dto.getDeliveryReadyItem().getCreatedAt())
            .createdBy(dto.getDeliveryReadyItem().getCreatedBy())
            .deliveryReadyFileCid(dto.getDeliveryReadyItem().getDeliveryReadyFileCid())
            .build();

        return viewVo;
    }
}
