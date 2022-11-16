package com.piaar_store_manager.server.domain.erp_order_item.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.piaar_store_manager.server.domain.erp_order_item.entity.ErpOrderItemEntity;

import com.piaar_store_manager.server.utils.CustomDateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Builder
@Getter
@Setter
@ToString
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class ErpOrderItemDto {
    private Integer cid;
    private UUID id;

//    @NotNull
//    @NotBlank
//    @Size(max = 36)
    private String uniqueCode; // 피아르 고유코드

    @NotBlank(message = "'상품명'을 입력해주세요.")
    @Size(max = 300, message = "'상품명'은 300자 이내로 입력해주세요.")
    private String prodName; // 상품명 / 필수값

    @NotBlank(message = "'옵션정보'를 입력해주세요.")
    @Size(max = 300, message = "'옵션정보'는 300자 이내로 입력해주세요.")
    private String optionName; // 옵션정보 / 필수값

    @Positive(message = "'수량'은 0보다 큰 수를 입력해주세요.")
    private Integer unit; // 수량 / 필수값

    @NotBlank(message = "'수취인명'을 입력해주세요.")
    @Size(max = 50, message = "'수취인명'은 50자 이내로 입력해주세요.")
    private String receiver; // 수취인명 / 필수값

    @NotBlank(message = "'전화번호1'을 입력해주세요.")
    @Size(max = 20, message = "'전화번호1'은 20자 이내로 입력해주세요.")
    private String receiverContact1; // 전화번호1 / 필수값

    @Size(max = 20, message = "'전화번호2'는 20자 이내로 입력해주세요.")
    private String receiverContact2; // 전화번호2

    @NotBlank(message = "'주소'를 입력해주세요.")
    @Size(max = 200, message = "'주소'는 200자 이내로 입력해주세요.")
    private String destination; // 주소 / 필수값

    @Size(max = 200, message = "'주소상세'는 200자 이내로 입력해주세요.")
    private String destinationDetail;

    @Size(max = 40, message = "'판매채널'은 40자 이내로 입력해주세요.")
    private String salesChannel; // 판매채널

    @Size(max = 36, message = "'판매채널 주문번호1'은 36자 이내로 입력해주세요.")
    private String orderNumber1; // 판매채널 주문번호1

    @Size(max = 36, message = "'판매채널 주문번호2'는 36자 이내로 입력해주세요.")
    private String orderNumber2; // 판매채널 주문번호2

    @Size(max = 36, message = "'판매채널 상품코드'는 36자 이내로 입력해주세요.")
    private String channelProdCode; // 판매채널 상품코드

    @Size(max = 36, message = "'판매채널 옵션코드'는 36자 이내로 입력해주세요.")
    private String channelOptionCode; // 판매채널 옵션코드

    @Size(max = 10, message = "'우편번호'는 10자 이내로 입력해주세요.")
    private String zipCode; // 우편번호

    @Size(max = 40, message = "'택배사'는 40자 이내로 입력해주세요.")
    private String courier; // 택배사

    @Size(max = 45, message = "'배송방식'은 45자 이내로 입력해주세요.")
    private String transportType; // 배송방식

    @Size(max = 200, message = "'배송메세지'는 200자 이내로 입력해주세요.")
    private String deliveryMessage; // 배송메세지

    @Size(max = 36, message = "'운송장번호'는 36자 이내로 입력해주세요.")
    private String waybillNumber;   // 운송장번호

    @PositiveOrZero(message = "'판매금액'은 0보다 작은 값을 입력할 수 없습니다.")
    private Integer price;  // 판매금액

    @PositiveOrZero(message = "'배송비'는 0보다 작은 값을 입력할 수 없습니다.")
    private Integer deliveryCharge;  // 배송비

    @Size(max = 100, message = "'바코드'는 100자 이내로 입력해주세요.")
    private String barcode; // 바코드

    @Size(max = 50, message = "'피아르 상품코드'는 50자 이내로 입력해주세요.")
    private String prodCode; // 피아르 상품코드

    @Size(max = 20, message = "'피아르 옵션코드'는 20자 이내로 입력해주세요.")
    private String optionCode; // 피아르 옵션코드

    @Size(max = 20, message = "'출고옵션코드'는 20자 이내로 입력해주세요.")
    private String releaseOptionCode;   // 출고 옵션코드

    @JsonDeserialize(using = CustomDateUtils.JsonLocalDateTimeBasicDeserializer.class)
    private LocalDateTime channelOrderDate;

    @Size(max = 200, message = "'관리메모1'은 200자 이내로 입력해주세요.")
    private String managementMemo1; // 관리메모1

    @Size(max = 200, message = "'관리메모2'는 200자 이내로 입력해주세요.")
    private String managementMemo2; // 관리메모2

    @Size(max = 200, message = "'관리메모3'은 200자 이내로 입력해주세요.")
    private String managementMemo3; // 관리메모3

    @Size(max = 200, message = "'관리메모4'는 200자 이내로 입력해주세요.")
    private String managementMemo4; // 관리메모4

    @Size(max = 200, message = "'관리메모5'은 200자 이내로 입력해주세요.")
    private String managementMemo5; // 관리메모5

    @Size(max = 200, message = "'관리메모6'은 200자 이내로 입력해주세요.")
    private String managementMemo6; // 관리메모6

    @Size(max = 200, message = "'관리메모7'은 200자 이내로 입력해주세요.")
    private String managementMemo7; // 관리메모7

    @Size(max = 200, message = "'관리메모8'은 200자 이내로 입력해주세요.")
    private String managementMemo8; // 관리메모8

    @Size(max = 200, message = "'관리메모9'는 200자 이내로 입력해주세요.")
    private String managementMemo9; // 관리메모9

    @Size(max = 200, message = "'관리메모10'은 200자 이내로 입력해주세요.")
    private String managementMemo10; // 관리메모10

    @Size(max = 200, message = "'운송코드'는 200자 이내로 입력해주세요.")
    private String freightCode; // 운송코드

    @Pattern(regexp = "^[n|y]$", message = "'판매 여부'에 올바른 값을 입력해주세요.")
    @Size(min = 1, max = 1)
    @Setter
    private String salesYn;

    @Setter
    private LocalDateTime salesAt;

    @Pattern(regexp = "^[n|y]$", message = "'출고 여부'에 올바른 값을 입력해주세요.")
    @Size(min = 1, max = 1)
    @Setter
    private String releaseYn;

    private LocalDateTime releaseAt;   // 출고등록일

    @Pattern(regexp = "^[n|y]$", message = "'재고반영 여부'에 올바른 값을 입력해주세요.")
    @Size(min = 1, max = 1)
    private String stockReflectYn;

    @Pattern(regexp = "^[n|y]$", message = "'반품 입금여부'에 올바른 값을 입력해주세요.")
    @Size(min = 1, max = 1)
    private String returnYn;
    // private String exchangeYn;
    private LocalDateTime createdAt;  // 주문등록일
    private UUID createdBy;

    private String categoryName;    // 피아르 카테고리명
    private String prodDefaultName; // 피아르 상품명
    private String prodManagementName;  // 피아르 상품설명
    private String optionDefaultName;   // 피아르 옵션명
    private String optionManagementName;    // 피아르 옵션설명
    private String optionReleaseLocation;    // 피아르 옵션설명
    private Integer optionStockUnit;    // 재고수량

    private Integer salesPrice; // 판매금액
    private Integer receiverDeliveryCharge;  // 소비자 부담 운임비
    private Integer purchaseCost;    // 매입금액
    private Integer purchaseDeliveryCharge; // 판매자 부담 운임비
    private Integer sellerDeliveryCharge;   // 판매자 부담 운임비
    private Integer extraCost;  // 기타비용
    private Integer commission; // 판매채널 수수료

    public static ErpOrderItemDto toDto(ErpOrderItemEntity entity) {
        ErpOrderItemDto dto = ErpOrderItemDto.builder()
                .cid(entity.getCid())
                .id(entity.getId())
                .uniqueCode(entity.getUniqueCode())
                .prodName(entity.getProdName())
                .optionName(entity.getOptionName())
                .unit(entity.getUnit())
                .receiver(entity.getReceiver())
                .receiverContact1(entity.getReceiverContact1())
                .receiverContact2(entity.getReceiverContact2())
                .destination(entity.getDestination())
                .salesChannel(entity.getSalesChannel())
                .orderNumber1(entity.getOrderNumber1())
                .orderNumber2(entity.getOrderNumber2())
                .channelProdCode(entity.getChannelProdCode())
                .channelOptionCode(entity.getChannelOptionCode())
                .zipCode(entity.getZipCode())
                .courier(entity.getCourier())
                .transportType(entity.getTransportType())
                .deliveryMessage(entity.getDeliveryMessage())
                .waybillNumber(entity.getWaybillNumber())
                .price(entity.getPrice())
                .deliveryCharge(entity.getDeliveryCharge())
                .barcode(entity.getBarcode())
                .prodCode(entity.getProdCode())
                .optionCode(entity.getOptionCode())
                .releaseOptionCode(entity.getReleaseOptionCode())
                .managementMemo1(entity.getManagementMemo1())
                .managementMemo2(entity.getManagementMemo2())
                .managementMemo3(entity.getManagementMemo3())
                .managementMemo4(entity.getManagementMemo4())
                .managementMemo5(entity.getManagementMemo5())
                .managementMemo6(entity.getManagementMemo6())
                .managementMemo7(entity.getManagementMemo7())
                .managementMemo8(entity.getManagementMemo8())
                .managementMemo9(entity.getManagementMemo9())
                .managementMemo10(entity.getManagementMemo10())
                .freightCode(entity.getFreightCode())
                .salesYn(entity.getSalesYn())
                .salesAt(entity.getSalesAt())
                .releaseYn(entity.getReleaseYn())
                .releaseAt(entity.getReleaseAt())
                .stockReflectYn(entity.getStockReflectYn())
                .returnYn(entity.getReturnYn())
                // .exchangeYn(entity.getExchangeYn())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .build();

        return dto;
    }
}
