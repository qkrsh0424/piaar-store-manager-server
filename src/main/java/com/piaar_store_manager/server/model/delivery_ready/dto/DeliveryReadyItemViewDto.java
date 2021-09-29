package com.piaar_store_manager.server.model.delivery_ready.dto;


import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyNaverItemEntity;

import lombok.Data;

@Data
public class DeliveryReadyItemViewDto {
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
