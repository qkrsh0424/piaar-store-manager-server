package com.piaar_store_manager.server.model.delivery_ready.piaar_ex.proj;

import com.piaar_store_manager.server.model.delivery_ready.piaar_ex.entity.DeliveryReadyPiaarItemEntity;

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
