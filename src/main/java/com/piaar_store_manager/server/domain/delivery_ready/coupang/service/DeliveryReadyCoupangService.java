package com.piaar_store_manager.server.domain.delivery_ready.coupang.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import com.piaar_store_manager.server.domain.delivery_ready.common.proj.DeliveryReadyItemOptionInfoProj;
import com.piaar_store_manager.server.domain.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;
import com.piaar_store_manager.server.domain.delivery_ready.coupang.proj.DeliveryReadyCoupangItemViewProj;
import com.piaar_store_manager.server.domain.delivery_ready.coupang.repository.DeliveryReadyCoupangItemRepository;
import com.piaar_store_manager.server.domain.delivery_ready_file.entity.DeliveryReadyFileEntity;
import com.piaar_store_manager.server.domain.delivery_ready_file.repository.DeliveryReadyFileRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryReadyCoupangService {
    private final DeliveryReadyFileRepository deliveryReadyFileRepository;
    private final DeliveryReadyCoupangItemRepository deliveryReadyCoupangItemRepository;

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
     * @param fileEntity : DeliveryReadyCoupangItemEntity
     * @return DeliveryReadyCoupangItemEntity
     * @see DeliveryReadyCoupangItemRepository#save
     */
    public DeliveryReadyCoupangItemEntity saveAndModifyOfItem(DeliveryReadyCoupangItemEntity fileEntity) {
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
    public List<DeliveryReadyCoupangItemEntity> saveAndModifyOfItemList(List<DeliveryReadyCoupangItemEntity> itemEntities) {
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
     * @see DeliveryReadyCoupangItemRepository#findUnreleasedItemList
     */
    public List<DeliveryReadyCoupangItemViewProj> findUnreleasedItemList() {
        return deliveryReadyCoupangItemRepository.findUnreleasedItemList();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyItem 중 특정 기간의 출고 데이터를 조회한다.
     *
     * @param startDate : Date
     * @param endDate : Date
     * @return List::DeliveryReadyCoupangItemViewProj::
     * @see DeliveryReadyCoupangItemRepository#findReleasedItemList
     */
    public List<DeliveryReadyCoupangItemViewProj> findReleasedItemList(LocalDateTime startDate, LocalDateTime endDate) {
        return deliveryReadyCoupangItemRepository.findReleasedItemList(startDate, endDate);
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
    public void deleteOneOfItem(Integer itemCid) {
        deliveryReadyCoupangItemRepository.findById(itemCid).ifPresent(item -> {
            deliveryReadyCoupangItemRepository.delete(item);
        });
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * DeliveryReadyItem 미출고 데이터 중 itemCid에 대응하는 데이터를 삭제한다.
     *
     * @param idList : List::UUID::
     * @see DeliveryReadyCoupangItemRepository#deleteBatchById
     */
    public void deleteListOfItem(List<UUID> idList) {
        deliveryReadyCoupangItemRepository.deleteBatchById(idList);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * DeliveryReadyCoupangItemEntity cid에 대응하는 데이터를 조회한다.
     *
     * @return DeliveryReadyCoupangItemEntity
     * @see DeliveryReadyCoupangItemRepository#findById
     */
    public DeliveryReadyCoupangItemEntity searchOneOfItem(Integer itemCid) {
        Optional<DeliveryReadyCoupangItemEntity> itemEntityOpt = deliveryReadyCoupangItemRepository.findById(itemCid);

        if (itemEntityOpt.isPresent()) {
            return itemEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
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
    public void updateReleasedInfoByCid(List<Integer> itemCids) {
        deliveryReadyCoupangItemRepository.updateReleasedInfoByCid(itemCids, CustomDateUtils.getCurrentDateTime());
    }
}
