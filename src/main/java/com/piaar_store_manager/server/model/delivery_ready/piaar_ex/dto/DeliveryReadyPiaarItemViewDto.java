package com.piaar_store_manager.server.model.delivery_ready.piaar_ex.dto;

import lombok.Data;

@Data
public class DeliveryReadyPiaarItemViewDto {
    private DeliveryReadyPiaarItemDto deliveryReadyItem;
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
