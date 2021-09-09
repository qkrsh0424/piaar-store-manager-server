package com.piaar_store_manager.server.model.delivery_ready.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeliveryReadyItemViewResDto {
    DeliveryReadyItemDto deliveryReadyItem;
    String optionDefaultName;
    String optionManagementName;
    Integer optionStockUnit;
    String prodManagementName;
}