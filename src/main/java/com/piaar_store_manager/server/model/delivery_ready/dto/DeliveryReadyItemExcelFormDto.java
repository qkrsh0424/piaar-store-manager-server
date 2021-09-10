package com.piaar_store_manager.server.model.delivery_ready.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeliveryReadyItemExcelFormDto {
    private String prodOrderNumber;     // 상품주문번호
    private String orderNumber;      // 주문번호
    private String receiver;        // 수취인명
    private String receiverContact1;        // 수취인연락처1
    private String zipCode;        // 우편번호
    private String destination;        // 배송지
    private String transportNumber;     // 운송장번호
    private String prodName;        // 상품명
    private String sender;      // 보내는사람
    private String senderContact1;  // 전화번호1(지정)
    private String optionInfo;      // 상품상세1
    private String optionManagementCode;        // 옵션관리코드
    private Integer unit;        // 내품수량1
    private String deliveryMessage;     // 배송메시지
    private String unitA;       // 수량(A타입)
    private String allProdOrderNumber;      // 총 상품주문번호

    private boolean duplication;    // 받는사람 + 번호 + 주소 : 중복 여부

    public static List<DeliveryReadyItemExcelFormDto> toFormDto(List<DeliveryReadyItemDto> dtos) {
        List<DeliveryReadyItemExcelFormDto> formDtos = new ArrayList<>();

        for(DeliveryReadyItemDto dto : dtos){
            DeliveryReadyItemExcelFormDto formDto = new DeliveryReadyItemExcelFormDto();

            formDto.setProdOrderNumber(dto.getProdOrderNumber()).setOrderNumber(dto.getOrderNumber())
                    .setReceiver(dto.getReceiver()).setReceiverContact1(dto.getReceiverContact1())
                    .setZipCode(dto.getZipCode()).setDestination(dto.getDestination())
                    .setTransportNumber("").setProdName(dto.getProdName())
                    .setSender("스토어명").setSenderContact1("070-0000-0000").setOptionInfo(dto.getOptionInfo())
                    .setOptionManagementCode(dto.getOptionManagementCode()).setUnit(dto.getUnit())
                    .setDeliveryMessage(dto.getDeliveryMessage()).setUnitA("").setAllProdOrderNumber(dto.getProdNumber()).setDuplication(false);

            formDtos.add(formDto);
        }

        return formDtos;
    }
}
