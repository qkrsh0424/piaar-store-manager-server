package com.piaar_store_manager.server.domain.delivery_ready.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class DeliveryReadyItemOptionInfoResDto {
    String optionCode;
    String optionDefaultName;
    String optionManagementName;
    String prodDefaultName;
}
