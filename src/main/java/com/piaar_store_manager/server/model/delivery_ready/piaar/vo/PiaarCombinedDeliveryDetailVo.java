package com.piaar_store_manager.server.model.delivery_ready.piaar.vo;

import java.util.List;

import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PiaarCombinedDeliveryDetailVo {
    // List<DeliveryReadyPiaarViewExcelItemVo> excelItem;
    List<DeliveryReadyPiaarItemDto> details;
}
