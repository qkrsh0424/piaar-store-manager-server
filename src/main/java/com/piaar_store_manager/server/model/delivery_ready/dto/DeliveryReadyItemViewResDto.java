package com.piaar_store_manager.server.model.delivery_ready.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryReadyItemViewResDto {
    DeliveryReadyItemDto deliveryReadyItem;
    String optionDefaultName;
    String optionManagementName;
    Integer optionStockUnit;
    String prodManagementName;
}
