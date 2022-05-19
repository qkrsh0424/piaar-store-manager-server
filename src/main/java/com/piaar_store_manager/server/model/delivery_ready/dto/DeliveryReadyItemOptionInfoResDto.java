package com.piaar_store_manager.server.model.delivery_ready.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeliveryReadyItemOptionInfoResDto {
    String optionCode;
    String optionDefaultName;
    String optionManagementName;
    String prodDefaultName;
}
