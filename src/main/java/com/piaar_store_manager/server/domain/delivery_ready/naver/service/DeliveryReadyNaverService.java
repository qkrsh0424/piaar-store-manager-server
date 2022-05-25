package com.piaar_store_manager.server.domain.delivery_ready.naver.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.domain.delivery_ready.common.proj.DeliveryReadyItemOptionInfoProj;
import com.piaar_store_manager.server.domain.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;
import com.piaar_store_manager.server.domain.delivery_ready.naver.proj.DeliveryReadyNaverItemViewProj;
import com.piaar_store_manager.server.domain.delivery_ready.naver.repository.DeliveryReadyNaverItemRepository;
import com.piaar_store_manager.server.domain.delivery_ready_file.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.domain.delivery_ready_file.repository.DeliveryReadyFileRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;
import com.piaar_store_manager.server.utils.CustomDateUtils;

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
    public DeliveryReadyFileEntity saveAndGetOfFile(DeliveryReadyFileEntity fileEntity) {
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
    public DeliveryReadyNaverItemEntity saveAndModifyOfItem(DeliveryReadyNaverItemEntity entity) {
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
    public List<DeliveryReadyNaverItemEntity> saveAndModifyOfItemList(List<DeliveryReadyNaverItemEntity> itemEntities) {
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
    public Set<String> findAllProdOrderNumber() {
        return deliveryReadyNaverItemRepository.findAllProdOrderNumber();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 미출고 데이터를 조회한다.
     *
     * @return List::DeliveryReadyNaverItemViewProj::
     * @see DeliveryReadyNaverItemRepository#findUnreleasedItemList
     */
    public List<DeliveryReadyNaverItemViewProj> findUnreleasedItemList() {
        return deliveryReadyNaverItemRepository.findUnreleasedItemList();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 특정 기간의 출고 데이터를 조회한다.
     *
     * @param startDate : Date
     * @param endDate : Date
     * @return List::DeliveryReadyNaverItemViewProj::
     * @see DeliveryReadyNaverItemRepository#findReleasedItemList
     */
    public List<DeliveryReadyNaverItemViewProj> findReleasedItemList(LocalDateTime startDate, LocalDateTime endDate) {
        return deliveryReadyNaverItemRepository.findReleasedItemList(startDate, endDate);
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
    public void deleteOneOfItem(Integer itemCid) {
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
    public void deleteListOfItem(List<UUID> idList) {
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
    public DeliveryReadyNaverItemEntity searchOneOfItem(Integer itemCid) {
        Optional<DeliveryReadyNaverItemEntity> itemEntityOpt = deliveryReadyNaverItemRepository.findById(itemCid);

        if (itemEntityOpt.isPresent()) {
            return itemEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyNaverItemEntity cid에 대응하는 데이터를 모두 조회한다.
     *
     * @return List::DeliveryReadyNaverItemEntity::
     * @see DeliveryReadyNaverItemRepository#selectAllByIdList
     */
    public List<DeliveryReadyNaverItemEntity> searchListById(List<UUID> idList) {
        return deliveryReadyNaverItemRepository.selectAllByIdList(idList);
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
     * @param itemCids : List::Integer::
     * @see DeliveryReadyNaverItemRepository#updateReleasedInfoByCid
     */
    @Transactional
    public void updateReleasedInfoByCid(List<Integer> itemCids) {
        deliveryReadyNaverItemRepository.updateReleasedInfoByCid(itemCids, CustomDateUtils.getCurrentDateTime());
    }

}
