package com.piaar_store_manager.server.model.delivery_ready.naver.dto;

import lombok.Data;

@Data
public class DeliveryReadyNaverItemViewDto {
    private DeliveryReadyNaverItemDto deliveryReadyItem;
    private String prodManufacturingCode;
    private String prodManagementName;
    private String optionDefaultName;
    private String optionManagementName;
    private Integer optionStockUnit;
    private String sender;
    private String senderContact1;
    private String optionNosUniqueCode;
    private String releaseMemo;
    private String receiveMemo;
}
