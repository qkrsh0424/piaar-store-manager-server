package com.piaar_store_manager.server.model.delivery_ready.dto;

import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewDto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class DeliveryReadyItemLotteExcelFormDto {
    private String receiver;    // 받는사람
    private String zipCode;     // 우편번호
    private String destination;     // 주소
    private String receiverContact1;    // 전화번호1
    private String receiverContact2;    // 전화번호2
    private String deliveryMessage;     // 배송메시지
    private String unnecessaryCell;    // 불필요한 항목
    private String sender;      // 보내는사람(지정)
    private String senderContact1;      // 전화번호1(지정)
    private String senderAddress;   // 주소(지정)
    private String unitA;   // 수량(A타입)
    private String prodName1;       // 상품명1
    private String optionInfo1;     // 상품상세1
    private Integer unit;        // 내품수량1
    private String orderNumber;     // 상품코드1
    private String prodOrderNumber;     // 상품코드2
    private String platformName;        // 상품상세2
    private String allProdOrderNumber;      // 총 상품주문번호
    private String allProdInfo;     // 상품명+옵션+수량

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyNaverItemViewDto => DeliveryReadyItemLotteExcelFormDto
     * 
     * @param viewDto : DeliveryReadyNaverItemViewDto
     * @return DeliveryReadyItemLotteExcelFormDto
     */
    public static DeliveryReadyItemLotteExcelFormDto toFormDto(DeliveryReadyNaverItemViewDto viewDto) {

        DeliveryReadyItemLotteExcelFormDto formDto = DeliveryReadyItemLotteExcelFormDto.builder()
            .receiver(viewDto.getDeliveryReadyItem().getReceiver())
            .zipCode(viewDto.getDeliveryReadyItem().getZipCode())
            .destination(viewDto.getDeliveryReadyItem().getDestination())
            .receiverContact1(viewDto.getDeliveryReadyItem().getReceiverContact1())
            .receiverContact2(viewDto.getDeliveryReadyItem().getReceiverContact2())
            .deliveryMessage(viewDto.getDeliveryReadyItem().getDeliveryMessage())
            .sender(viewDto.getSender())
            .senderContact1(viewDto.getSenderContact1())
            .senderAddress("서울특별시 마포구 와우산로 11길 25 3층")
            .unitA("")
            .prodName1(viewDto.getDeliveryReadyItem().getProdName())
            .optionInfo1(viewDto.getDeliveryReadyItem().getOptionInfo())
            .unit(viewDto.getDeliveryReadyItem().getUnit())
            .orderNumber(viewDto.getDeliveryReadyItem().getOrderNumber())
            .prodOrderNumber(viewDto.getDeliveryReadyItem().getProdOrderNumber())
            .platformName("네이버")
            .allProdOrderNumber(viewDto.getDeliveryReadyItem().getProdOrderNumber())
            .allProdInfo(viewDto.getDeliveryReadyItem().getProdName() + " [" + viewDto.getDeliveryReadyItem().getOptionInfo() + "-" + viewDto.getDeliveryReadyItem().getUnit() + "]")
            .build();

        return formDto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyCoupangItemViewDto => DeliveryReadyItemLotteExcelFormDto
     * 
     * @param viewDto : DeliveryReadyCoupangItemViewDto
     * @return DeliveryReadyItemLotteExcelFormDto
     */
    public static DeliveryReadyItemLotteExcelFormDto toFormDto(DeliveryReadyCoupangItemViewDto viewDto) {
        DeliveryReadyItemLotteExcelFormDto formDto = DeliveryReadyItemLotteExcelFormDto.builder()
            .receiver(viewDto.getDeliveryReadyItem().getReceiver())
            .zipCode(viewDto.getDeliveryReadyItem().getZipCode())
            .destination(viewDto.getDeliveryReadyItem().getDestination())
            .receiverContact1(viewDto.getDeliveryReadyItem().getReceiverContact1())
            .deliveryMessage(viewDto.getDeliveryReadyItem().getDeliveryMessage())
            .sender(viewDto.getSender())
            .senderContact1(viewDto.getSenderContact1())
            .senderAddress("서울특별시 마포구 와우산로 11길 25 3층")
            .unitA("")
            .prodName1(viewDto.getDeliveryReadyItem().getProdName())
            .optionInfo1(viewDto.getDeliveryReadyItem().getOptionInfo())
            .unit(viewDto.getDeliveryReadyItem().getUnit())
            .orderNumber(viewDto.getDeliveryReadyItem().getOrderNumber())
            .prodOrderNumber(viewDto.getDeliveryReadyItem().getProdOrderNumber())
            .platformName("쿠팡")
            .allProdOrderNumber(viewDto.getDeliveryReadyItem().getProdOrderNumber())
            .allProdInfo(viewDto.getDeliveryReadyItem().getProdName() + " [" + viewDto.getDeliveryReadyItem().getOptionInfo() + "-" + viewDto.getDeliveryReadyItem().getUnit() + "]")
            .build();

        return formDto;
    }
}
