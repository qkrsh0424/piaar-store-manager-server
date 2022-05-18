package com.piaar_store_manager.server.domain.order_registration.naver.dto;

import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemHansanExcelFormDto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class OrderRegistrationNaverFormDto {
    private String prodOrderNumber;     // 상품주문번호
    private String transportType;       // 배송방법
    private String deliveryService;     // 택배사
    private String transportNumber;     // 송장번호

    public static OrderRegistrationNaverFormDto toNaverFormDto(DeliveryReadyItemHansanExcelFormDto dto) {
        OrderRegistrationNaverFormDto  formDto = OrderRegistrationNaverFormDto.builder()
            .prodOrderNumber(dto.getProdOrderNumber())
            .transportType("택배,등기,소포")
            .deliveryService("롯데택배")
            .transportNumber(dto.getTransportNumber())
            .build();

        return formDto;
    }
}
