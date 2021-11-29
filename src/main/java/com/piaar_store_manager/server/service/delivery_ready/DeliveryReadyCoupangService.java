package com.piaar_store_manager.server.service.delivery_ready;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.coupang.proj.DeliveryReadyCoupangItemViewProj;
import com.piaar_store_manager.server.model.delivery_ready.coupang.repository.DeliveryReadyCoupangItemRepository;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.model.delivery_ready.proj.DeliveryReadyItemOptionInfoProj;
import com.piaar_store_manager.server.model.delivery_ready.repository.DeliveryReadyFileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryReadyCoupangService {
    private DeliveryReadyFileRepository deliveryReadyFileRepository;
    private DeliveryReadyCoupangItemRepository deliveryReadyCoupangItemRepository;

    @Autowired
    public DeliveryReadyCoupangService(
        DeliveryReadyFileRepository deliveryReadyFileRepository,
        DeliveryReadyCoupangItemRepository deliveryReadyCoupangItemRepository
    ) {
        this.deliveryReadyFileRepository = deliveryReadyFileRepository;
        this.deliveryReadyCoupangItemRepository = deliveryReadyCoupangItemRepository;
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
    public DeliveryReadyFileEntity createFile(DeliveryReadyFileEntity fileEntity) {
        return deliveryReadyFileRepository.save(fileEntity);
    }
    
    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일의 단일 데이터를 저장한다.
     *
     * @param fileEntity : DeliveryReadyCoupangItemEntity
     * @return DeliveryReadyCoupangItemEntity
     * @see DeliveryReadyCoupangItemRepository#save
     */
    public DeliveryReadyCoupangItemEntity createItem(DeliveryReadyCoupangItemEntity fileEntity) {
        return deliveryReadyCoupangItemRepository.save(fileEntity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일의 모든 데이터를 저장한다.
     *
     * @param itemEntities : List::DeliveryReadyCoupangItemEntity::
     * @return List::DeliveryReadyCoupangItemEntity::
     * @see DeliveryReadyCoupangItemRepository#saveAll
     */
    public List<DeliveryReadyCoupangItemEntity> createItemList(List<DeliveryReadyCoupangItemEntity> itemEntities) {
        return deliveryReadyCoupangItemRepository.saveAll(itemEntities);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 배송준비 엑셀 데이터에서 상품 주문번호를 모두 조회한다.
     * 
     * @return Set::String
     * @see DeliveryReadyCoupangItemRepository#findAllProdOrderNumber
     */
    public Set<String> findAllProdOrderNumber() {
        return deliveryReadyCoupangItemRepository.findAllProdOrderNumber();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 미출고 데이터를 조회한다.
     *
     * @return List::DeliveryReadyCoupangItemViewProj::
     * @see DeliveryReadyCoupangItemRepository#findSelectedUnreleased
     */
    public List<DeliveryReadyCoupangItemViewProj> findSelectedUnreleased() {
        return deliveryReadyCoupangItemRepository.findSelectedUnreleased();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 특정 기간의 출고 데이터를 조회한다.
     *
     * @param startDate : Date
     * @param endDate : Date
     * @return List::DeliveryReadyCoupangItemViewProj::
     * @see DeliveryReadyCoupangItemRepository#findSelectedReleased
     */
    public List<DeliveryReadyCoupangItemViewProj> findSelectedReleased(Date startDate, Date endDate) {
        return deliveryReadyCoupangItemRepository.findSelectedReleased(startDate, endDate);
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 itemCid에 대응하는 데이터를 삭제한다.
     *
     * @param itemCid : Integer
     * @see DeliveryReadCoupangItemRepository#findById
     * @see DeliveryReadyCoupangItemRepository#delete
     */
    public void deleteOneDeliveryReadyViewData(Integer itemCid) {
        deliveryReadyCoupangItemRepository.findById(itemCid).ifPresent(item -> {
            deliveryReadyCoupangItemRepository.delete(item);
        });
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyCoupangItemEntity cid에 대응하는 데이터를 조회한다.
     *
     * @return DeliveryReadyCoupangItemEntity
     * @see DeliveryReadyCoupangItemRepository#findById
     */
    public DeliveryReadyCoupangItemEntity searchDeliveryReadyItem(Integer itemCid) {
        Optional<DeliveryReadyCoupangItemEntity> itemEntityOpt = deliveryReadyCoupangItemRepository.findById(itemCid);

        if (itemEntityOpt.isPresent()) {
            return itemEntityOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyCoupangItemEntity cid에 대응하는 데이터를 모두 조회한다.
     *
     * @return List::DeliveryReadyCoupangItemEntity::
     * @see DeliveryReadyCoupangItemRepository#selectAllByCids
     */
    public List<DeliveryReadyCoupangItemEntity> searchDeliveryReadyItemList(List<Integer> itemCids) {
        return deliveryReadyCoupangItemRepository.selectAllByCids(itemCids);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 모든 상품의 옵션정보들을 조회한다.
     *
     * @return List::DeliveryReadyItemOptionInfoProj::
     * @see DeliveryReadyCoupangItemRepository#findAllOptionInfo
     */
    public List<DeliveryReadyItemOptionInfoProj> findAllOptionInfo() {
        return deliveryReadyCoupangItemRepository.findAllOptionInfo();
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 prodName(상품명), optionInfo(옵션명)에 대응하는 데이터와 동일한 상품들의 옵션관리코드를 일괄 수정한다.
     *
     * @param entity : DeliveryReadyCoupangItemEntity
     * @return List::DeliveryReadyCoupangItemEntity::
     * @see DeliveryReadyCoupangItemRepository#findByItems
     */
    public List<DeliveryReadyCoupangItemEntity> findByItems(DeliveryReadyCoupangItemEntity entity) {
        return deliveryReadyCoupangItemRepository.findByItems(entity.getProdName(), entity.getOptionInfo());
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 데이터 다운로드 시 출고 정보를 설정한다.
     *
     * @param dtos : List::DeliveryReadyCoupangItemViewDto::
     * @see DeliveryReadyCoupangItemEntity#toEntity
     * @see DeliveryReadyCoupangItemRepository#updateReleasedAtByCid
     */
    @Transactional
    public void updateReleasedAtByCid(List<Integer> itemCids) {
        deliveryReadyCoupangItemRepository.updateReleasedAtByCid(itemCids, DateHandler.getCurrentDate2());
    }
}
