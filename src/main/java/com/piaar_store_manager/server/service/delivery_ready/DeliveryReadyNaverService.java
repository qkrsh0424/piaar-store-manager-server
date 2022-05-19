package com.piaar_store_manager.server.service.delivery_ready;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.model.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;
import com.piaar_store_manager.server.model.delivery_ready.naver.proj.DeliveryReadyNaverItemViewProj;
import com.piaar_store_manager.server.model.delivery_ready.naver.repository.DeliveryReadyNaverItemRepository;
import com.piaar_store_manager.server.model.delivery_ready.proj.DeliveryReadyItemOptionInfoProj;
import com.piaar_store_manager.server.model.delivery_ready.repository.DeliveryReadyFileRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryReadyNaverService {
    private final DeliveryReadyFileRepository deliveryReadyFileRepository;
    private final DeliveryReadyNaverItemRepository deliveryReadyNaverItemRepository;
 
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
     * @param entity : DeliveryReadyNaverItemEntity
     * @return DeliveryReadyNaverItemEntity
     * @see DeliveryReadyNaverItemRepository#save
     */
    public DeliveryReadyNaverItemEntity createItem(DeliveryReadyNaverItemEntity entity) {
        return deliveryReadyNaverItemRepository.save(entity);
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * 배송준비 엑셀 파일의 모든 데이터를 저장한다.
     *
     * @param itemEntities : List::DeliveryReadyNaverItemEntity::
     * @return List::DeliveryReadyNaverItemEntity::
     * @see DeliveryReadyNaverItemRepository#saveAll
     */
    public List<DeliveryReadyNaverItemEntity> createItemList(List<DeliveryReadyNaverItemEntity> itemEntities) {
        return deliveryReadyNaverItemRepository.saveAll(itemEntities);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 배송준비 엑셀 데이터에서 상품 주문번호를 모두 조회한다.
     * 
     * @return Set::String
     * @see DeliveryReadyNaverItemRepository#findAllProdOrderNumber
     */
    public Set<String> findAllProdOrderNubmer() {
        return deliveryReadyNaverItemRepository.findAllProdOrderNumber();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 미출고 데이터를 조회한다.
     *
     * @return List::DeliveryReadyNaverItemViewProj::
     * @see DeliveryReadyNaverItemRepository#findSelectedUnreleased
     */
    public List<DeliveryReadyNaverItemViewProj> findSelectedUnreleased() {
        return deliveryReadyNaverItemRepository.findSelectedUnreleased();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 특정 기간의 출고 데이터를 조회한다.
     *
     * @param startDate : Date
     * @param endDate : Date
     * @return List::DeliveryReadyNaverItemViewProj::
     * @see DeliveryReadyNaverItemRepository#findSelectedReleased
     */
    public List<DeliveryReadyNaverItemViewProj> findSelectedReleased(Date startDate, Date endDate) {
        return deliveryReadyNaverItemRepository.findSelectedReleased(startDate, endDate);
    }
 
    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 itemCid에 대응하는 데이터를 삭제한다.
     *
     * @param itemCid : Integer
     * @see DeliveryReadyNaverItemRepository#findById
     * @see DeliveryReadyNaverItemRepository#delete
     */
    public void deleteOneDeliveryReadyViewData(Integer itemCid) {
        deliveryReadyNaverItemRepository.findById(itemCid).ifPresent(item -> {
            deliveryReadyNaverItemRepository.delete(item);
        });
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 itemCid에 대응하는 데이터를 삭제한다.
     *
     * @param idList : List::UUID::
     * @see DeliveryReadyNaverItemRepository#deleteBatchById
     */
    public void deleteListDeliveryReadyViewData(List<UUID> idList) {
        deliveryReadyNaverItemRepository.deleteBatchById(idList);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyNaverItemEntity cid에 대응하는 데이터를 조회한다.
     *
     * @return DeliveryReadyNaverItemEntity
     * @see DeliveryReadyNaverItemRepository#findById
     */
    public DeliveryReadyNaverItemEntity searchDeliveryReadyItem(Integer itemCid) {
        Optional<DeliveryReadyNaverItemEntity> itemEntityOpt = deliveryReadyNaverItemRepository.findById(itemCid);

        if (itemEntityOpt.isPresent()) {
            return itemEntityOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyNaverItemEntity cid에 대응하는 데이터를 모두 조회한다.
     *
     * @return List::DeliveryReadyNaverItemEntity::
     * @see DeliveryReadyNaverItemRepository#selectAllByCids
     */
    public List<DeliveryReadyNaverItemEntity> searchDeliveryReadyItemList(List<Integer> itemCids) {
        return deliveryReadyNaverItemRepository.selectAllByCids(itemCids);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 등록된 모든 상품의 옵션정보들을 조회한다.
     *
     * @return List::DeliveryReadyItemOptionInfoProj::
     * @see DeliveryReadyNaverItemRepository#findAllOptionInfo
     */
    public List<DeliveryReadyItemOptionInfoProj> findAllOptionInfo() {
        return deliveryReadyNaverItemRepository.findAllOptionInfo();
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * DeliveryReadyItem의 데이터 prodName(상품명), optionInfo(옵션명)에 대응하는 데이터와 동일한 상품들의 옵션관리코드를 일괄 수정한다.
     *
     * @param entity : DeliveryReadyNaverItemEntity
     * @return List::DeliveryReadyNaverItemEntity::
     * @see DeliveryReadyNaverItemRepository#findByItems
     */
    public List<DeliveryReadyNaverItemEntity> findByItems(DeliveryReadyNaverItemEntity entity) {
        return deliveryReadyNaverItemRepository.findByItems(entity.getProdName(), entity.getOptionInfo());
    }

    /**
     * <b>DB Update Related Method</b>
     * <p>
     * 데이터 다운로드 시 출고 정보를 설정한다.
     *
     * @param dtos : List::DeliveryReadyNaverItemViewDto::
     * @see DeliveryReadyNaverItemEntity#toEntity
     * @see DeliveryReadyNaverItemRepository#updateReleasedAtByCid
     */
    @Transactional
    public void updateReleasedAtByCid(List<Integer> itemCids) {
        deliveryReadyNaverItemRepository.updateReleasedAtByCid(itemCids, DateHandler.getCurrentDate2());
    }

}
