package com.piaar_store_manager.server.model.delivery_ready.dto;

import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewDto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class DeliveryReadyItemTailoExcelFormDto {
    private String prodUniqueCode; // 상품고유코드
    private String salesProdName; // 판매상품명
    private Integer unit; // 수량
    private String transportType; // 배송방식
    private String buyer; // 주문자 이름
    private String receiver; // 받는분 이름
    private String receiverContact1; // 전화번호1
    private String receiverContact2; // 전화번호2
    private String zipCode; // 우편번호
    private String destination1; // 주소1
    private String destination2; // 주소1
    private String deliveryMessage; // 배송메세지
    private String orderNumber; // 주문번호
    private String managementMemo1; // 관리메모1
    private String managementMemo2;   // 관리메모2
    private String managementMemo3; // 관리메모3
    private String managementMemo4; // 관리메모4
    private String managementMemo5; // 관리메모5
    private String prodMemo1; // 상품별 메모1
    private String prodMemo2; // 상품별 메모2
    private String prodMemo3; // 상품별 메모3
    private String orderType; // 발주 타입
    private String releaseDesiredDate; // 출고희망일
    private String deliveryService; // 택배사
    private String transportNumber; // 송장번호

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyNaverItemViewDto => DeliveryReadyItemTailoExcelFormDto
     * 
     * @param viewDto : DeliveryReadyNaverItemViewDto
     * @return DeliveryReadyItemTailoExcelFormDto
     */
    public static DeliveryReadyItemTailoExcelFormDto toTailoFormDto(DeliveryReadyNaverItemViewDto viewDto) {
        DeliveryReadyItemTailoExcelFormDto formDto = DeliveryReadyItemTailoExcelFormDto.builder()
                .prodUniqueCode((viewDto.getOptionNosUniqueCode() != null && !viewDto.getOptionNosUniqueCode().trim().isEmpty()) ? viewDto.getOptionNosUniqueCode() : "*지정바람")
                .salesProdName((viewDto.getProdManagementName() != null && !viewDto.getProdManagementName().trim().isEmpty()) ? viewDto.getProdManagementName() + "|" + viewDto.getOptionManagementName() : "*지정바람")
                .unit(viewDto.getDeliveryReadyItem().getUnit())
                .transportType("택배,등기,소포")
                .buyer(viewDto.getDeliveryReadyItem().getBuyer())
                .receiver(viewDto.getDeliveryReadyItem().getReceiver())
                .receiverContact1(viewDto.getDeliveryReadyItem().getReceiverContact1())
                .receiverContact2(viewDto.getDeliveryReadyItem().getReceiverContact2())
                .zipCode(viewDto.getDeliveryReadyItem().getZipCode())
                .destination1(viewDto.getDeliveryReadyItem().getDestination())
                .deliveryMessage(viewDto.getDeliveryReadyItem().getDeliveryMessage())
                .orderNumber(viewDto.getDeliveryReadyItem().getOrderNumber())
                .managementMemo3("네이버")
                .prodMemo1(viewDto.getDeliveryReadyItem().getProdOrderNumber())
                .prodMemo2(viewDto.getDeliveryReadyItem().getProdName() + "|" + viewDto.getDeliveryReadyItem().getOptionInfo())
                .prodMemo3(viewDto.getDeliveryReadyItem().getOptionManagementCode())
                .build();

        return formDto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyCoupangItemViewDto => DeliveryReadyItemTailoExcelFormDto
     * 
     * @param viewDto : DeliveryReadyCoupangItemViewDto
     * @return DeliveryReadyItemTailoExcelFormDto
     */
    public static DeliveryReadyItemTailoExcelFormDto toTailoFormDto(DeliveryReadyCoupangItemViewDto viewDto) {
        DeliveryReadyItemTailoExcelFormDto formDto = DeliveryReadyItemTailoExcelFormDto.builder()
                .prodUniqueCode((viewDto.getOptionNosUniqueCode() != null && !viewDto.getOptionNosUniqueCode().trim().isEmpty()) ? viewDto.getOptionNosUniqueCode() : "*지정바람")
                .salesProdName((viewDto.getProdManagementName() != null && !viewDto.getProdManagementName().trim().isEmpty()) ? viewDto.getProdManagementName() + "|" + viewDto.getOptionManagementName() : "*지정 바람")
                .unit(viewDto.getDeliveryReadyItem().getUnit())
                .transportType("택배,등기,소포")
                .buyer(viewDto.getDeliveryReadyItem().getBuyer())
                .receiver(viewDto.getDeliveryReadyItem().getReceiver())
                .receiverContact1(viewDto.getDeliveryReadyItem().getReceiverContact1())
                .zipCode(viewDto.getDeliveryReadyItem().getZipCode())
                .destination1(viewDto.getDeliveryReadyItem().getDestination())
                .deliveryMessage(viewDto.getDeliveryReadyItem().getDeliveryMessage())
                .orderNumber(viewDto.getDeliveryReadyItem().getOrderNumber())
                .managementMemo3("쿠팡")
                .prodMemo1(viewDto.getDeliveryReadyItem().getProdOrderNumber())
                .prodMemo2(viewDto.getDeliveryReadyItem().getProdName() + "|" + viewDto.getDeliveryReadyItem().getOptionInfo())
                .prodMemo3(viewDto.getDeliveryReadyItem().getOptionManagementCode())
                .build();

        return formDto;
    }
}
