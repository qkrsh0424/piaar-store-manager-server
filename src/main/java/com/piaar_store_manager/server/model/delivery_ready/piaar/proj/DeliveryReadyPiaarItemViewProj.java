package com.piaar_store_manager.server.model.delivery_ready.piaar.proj;

import com.piaar_store_manager.server.model.delivery_ready.piaar.entity.DeliveryReadyPiaarItemEntity;

public interface DeliveryReadyPiaarItemViewProj {
    DeliveryReadyPiaarItemEntity getDeliveryReadyItem();
    String getProdManufacturingCode();
    String getOptionDefaultName();
    String getOptionManagementName();
    Integer getOptionStockUnit();
    String getOptionMemo();
    String getProdManagementName();
    String getOptionNosUniqueCode();
}
