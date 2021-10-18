package com.piaar_store_manager.server.model.order_registration.naver;

import java.util.List;

import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemTailoExcelFormDto;

import lombok.Data;

@Data
public class OrderRegistrationTailoDownloadFormDto {
    private List<DeliveryReadyItemTailoExcelFormDto> sendedDto;     // 피아르 테일로 엑셀 다운로드 파일
    private List<DeliveryReadyItemTailoExcelFormDto> receivedDto;       // 테일로 송장기입된 파일
}
