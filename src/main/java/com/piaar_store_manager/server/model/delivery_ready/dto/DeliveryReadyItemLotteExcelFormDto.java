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
    private Integer unitA;   // 수량(A타입)
    private String prodName1;       // 상품명1
    private String optionInfo1;     // 상품상세1
    private String prodName2;       // 상품명2
    private String unit;        // 내품수량1
    private String transportNumber;     // 운송장번호
    private String purchaseDate;        // 날짜

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
            .senderAddress("*지정바람")
            .unitA(viewDto.getDeliveryReadyItem().getUnit())
            .prodName1(viewDto.getDeliveryReadyItem().getProdName())
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
            .senderAddress("*지정바람")
            .unitA(viewDto.getDeliveryReadyItem().getUnit())
            .prodName1(viewDto.getDeliveryReadyItem().getProdName())
            .build();

        return formDto;
    }
}
