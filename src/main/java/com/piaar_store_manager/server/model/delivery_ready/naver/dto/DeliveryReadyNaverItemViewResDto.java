package com.piaar_store_manager.server.model.delivery_ready.naver.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeliveryReadyNaverItemViewResDto {
    DeliveryReadyNaverItemDto deliveryReadyItem;
    String prodManufacturingCode;
    String prodManagementName;
    String optionDefaultName;
    String optionManagementName;
    Integer optionStockUnit;
    String sender;
    String senderContact1;
    String optionNosUniqueCode;
}