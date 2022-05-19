package com.piaar_store_manager.server.domain.delivery_ready.naver.proj;

import com.piaar_store_manager.server.domain.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;

public interface DeliveryReadyNaverItemViewProj {
    DeliveryReadyNaverItemEntity getDeliveryReadyItem();
    String getProdManufacturingCode();
    String getProdManagementName();
    String getOptionDefaultName();
    String getOptionManagementName();
    Integer getOptionStockUnit();
    String getOptionMemo();
    String getOptionNosUniqueCode();
}