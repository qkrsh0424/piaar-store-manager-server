package com.piaar_store_manager.server.model.delivery_ready.coupang.proj;

import com.piaar_store_manager.server.model.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;

public interface DeliveryReadyCoupangItemViewProj {
    DeliveryReadyCoupangItemEntity getDeliveryReadyItem();
    String getProdManufacturingCode();
    String getOptionDefaultName();
    String getOptionManagementName();
    Integer getOptionStockUnit();
    String getOptionMemo();
    String getProdManagementName();
    String getOptionNosUniqueCode();
}