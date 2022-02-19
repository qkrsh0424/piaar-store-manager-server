package com.piaar_store_manager.server.model.delivery_ready.piaar.vo;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class PiaarCombinedDeliveryExcelItemVo {
    private UUID id;
    PiaarCombinedDeliveryDetailVo combinedDelivery = new PiaarCombinedDeliveryDetailVo();
}
