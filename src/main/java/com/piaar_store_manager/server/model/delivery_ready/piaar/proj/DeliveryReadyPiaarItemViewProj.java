package com.piaar_store_manager.server.model.delivery_ready.piaar.proj;

import com.piaar_store_manager.server.model.delivery_ready.piaar.entity.DeliveryReadyPiaarItemEntity;

public interface DeliveryReadyPiaarItemViewProj {
    DeliveryReadyPiaarItemEntity getDeliveryReadyItem();
    String getProdDefaultName();
    String getOptionDefaultName();
    String getProdManagementName();
    String getOptionManagementName();
    String getCategoryName();
    // 재고수량은 dto에서 설정
}
