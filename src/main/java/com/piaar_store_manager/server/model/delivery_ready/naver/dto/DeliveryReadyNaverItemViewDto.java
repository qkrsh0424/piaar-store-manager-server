package com.piaar_store_manager.server.model.delivery_ready.naver.dto;


import com.piaar_store_manager.server.model.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;

import lombok.Data;

@Data
public class DeliveryReadyNaverItemViewDto {
    private DeliveryReadyNaverItemEntity deliveryReadyItem;
    private String prodManufacturingCode;
    private String prodManagementName;
    private String optionDefaultName;
    private String optionManagementName;
    private String optionStockUnit;
    private String sender;
    private String senderContact1;
    private String optionNosUniqueCode;
}
