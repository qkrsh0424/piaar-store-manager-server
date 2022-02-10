package com.piaar_store_manager.server.service.delivery_ready;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.model.delivery_ready.piaar_ex.entity.DeliveryReadyPiaarItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.piaar_ex.repository.DeliveryReadyPiaarItemRepository;
import com.piaar_store_manager.server.model.delivery_ready.repository.DeliveryReadyFileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReadyPiaarService {
    private DeliveryReadyFileRepository deliveryReadyFileRepository;
    private DeliveryReadyPiaarItemRepository deliveryReadyPiaarItemRepository;

    @Autowired
    public DeliveryReadyPiaarService(
        DeliveryReadyFileRepository deliveryReadyFileRepository,
        DeliveryReadyPiaarItemRepository deliveryReadyPiaarItemRepository
    ) {
        this.deliveryReadyFileRepository = deliveryReadyFileRepository;
        this.deliveryReadyPiaarItemRepository = deliveryReadyPiaarItemRepository;   
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일의 정보를 저장한다.
     *
     * @param fileEntity : DeliveryReadyFileEntity
     * @return DeliveryReadyFileEntity
     * @see DeliveryReadyFileRepository#save
     */
    public DeliveryReadyFileEntity saveFile(DeliveryReadyFileEntity fileEntity) {
        return deliveryReadyFileRepository.save(fileEntity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일의 모든 데이터를 저장한다.
     *
     * @param itemEntities : List::DeliveryReadyPiaarItemEntity::
     * @return List::DeliveryReadyPiaarItemEntity::
     * @see DeliveryReadyPiaarItemRepository#saveAll
     */
    public List<DeliveryReadyPiaarItemEntity> saveItemList(List<DeliveryReadyPiaarItemEntity> itemEntities) {
        return deliveryReadyPiaarItemRepository.saveAll(itemEntities);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 주문현황 데이터를 조회한다.
     *
     * @param userId : UUID
     * @return List::DeliveryReadyPiaarItemEntity::
     * @see DeliveryReadyPiaarItemRepository#searchOrderListByUser
     */
    public List<DeliveryReadyPiaarItemEntity> searchOrderListByUser(UUID userId) {
        return deliveryReadyPiaarItemRepository.searchOrderListByUser(userId);
    }
}
