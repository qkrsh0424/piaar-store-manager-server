package com.piaar_store_manager.server.service.delivery_ready_view_header;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.entity.DeliveryReadyPiaarViewHeaderEntity;
import com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.repository.DeliveryReadyPiaarViewHeaderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReadyPiaarViewHeaderService {
    private DeliveryReadyPiaarViewHeaderRepository deliveryReadyPiaarViewHeaderRepository;

    @Autowired
    public DeliveryReadyPiaarViewHeaderService(
        DeliveryReadyPiaarViewHeaderRepository deliveryReadyPiaarViewHeaderRepository
    ) {
        this.deliveryReadyPiaarViewHeaderRepository = deliveryReadyPiaarViewHeaderRepository;   
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * Product 내용을 한개 등록한다.
     * 
     * @param entity : ProductEntity
     * @see ProductRepository#save
     */
    public DeliveryReadyPiaarViewHeaderEntity saveOne(DeliveryReadyPiaarViewHeaderEntity entity) {
        return deliveryReadyPiaarViewHeaderRepository.save(entity);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * Product cid 값과 상응되는 데이터를 조회한다.
     *
     * @param userId : UUID
     * @return DeliveryReadyPiaarViewHeaderEntity
     * @see DeliveryReadyPiaarViewHeaderRepository#searchOneByUser
     */
    public DeliveryReadyPiaarViewHeaderEntity searchOneByUser(UUID userId) {
        Optional<DeliveryReadyPiaarViewHeaderEntity> viewHeaderEntityOpt = deliveryReadyPiaarViewHeaderRepository.searchOneByUser(userId);

        if (viewHeaderEntityOpt.isPresent()) {
            return viewHeaderEntityOpt.get();
        } else {
            throw new NullPointerException();
        }
    }
}
