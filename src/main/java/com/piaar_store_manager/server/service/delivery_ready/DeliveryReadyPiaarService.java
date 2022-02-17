package com.piaar_store_manager.server.service.delivery_ready;

import java.util.List;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.model.delivery_ready.piaar.dto.DeliveryReadyPiaarItemDto;
import com.piaar_store_manager.server.model.delivery_ready.piaar.entity.DeliveryReadyPiaarItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.piaar.proj.DeliveryReadyPiaarItemViewProj;
import com.piaar_store_manager.server.model.delivery_ready.piaar.repository.DeliveryReadyPiaarItemRepository;
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
     * 배송준비 엑셀 파일의 하나의 데이터를 저장한다.
     *
     * @param itemEntity : DeliveryReadyPiaarItemEntity
     * @return DeliveryReadyPiaarItemEntity
     * @see DeliveryReadyPiaarItemRepository#save
     */
    public List<DeliveryReadyPiaarItemEntity> saveItemList(List<DeliveryReadyPiaarItemEntity> itemEntities) {
        return deliveryReadyPiaarItemRepository.saveAll(itemEntities);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * userId에 대응하는 유저의 주문현황 데이터를 조회한다.
     * 옵션코드와 관련된 데이터들을 함께 조회한다.
     *
     * @param userId : UUID
     * @return List::DeliveryReadyPiaarItemViewProj::
     * @see DeliveryReadyPiaarItemRepository#findMappingDataByPiaarOptionCodeAndUser
     */
    public List<DeliveryReadyPiaarItemViewProj> findMappingDataByPiaarOptionCodeAndUser(UUID userId) {
        return deliveryReadyPiaarItemRepository.findMappingDataByPiaarOptionCodeAndUser(userId);
    }

    public List<DeliveryReadyPiaarItemEntity> searchDeliveryReadyItemList(List<Integer> itemCids) {
        return deliveryReadyPiaarItemRepository.selectAllByCids(itemCids);
    }
}
