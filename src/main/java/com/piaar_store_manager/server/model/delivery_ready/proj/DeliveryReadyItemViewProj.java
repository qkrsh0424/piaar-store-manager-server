package com.piaar_store_manager.server.model.delivery_ready.proj;

import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyItemEntity;

public interface DeliveryReadyItemViewProj {
    DeliveryReadyItemEntity getDeliveryReadyItem();
    String getOptionDefaultName();
    String getOptionManagementName();
    Integer getOptionStockUnit();
    String getProdManagementName();
}