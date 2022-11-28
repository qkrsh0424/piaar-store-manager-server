package com.piaar_store_manager.server.domain.product_receive.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProjection;
import com.piaar_store_manager.server.domain.product_receive.repository.ProductReceiveCustomJdbc;
import com.piaar_store_manager.server.domain.product_receive.repository.ProductReceiveRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductReceiveService {
    private final ProductReceiveRepository productReceiveRepository;
    private final ProductReceiveCustomJdbc productReceiveCustomJdbc;

    public ProductReceiveEntity searchOne(UUID receiveId) {
        Optional<ProductReceiveEntity> entityOpt = productReceiveRepository.findById(receiveId);

        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public void saveAndModify(ProductReceiveEntity entity) {
        productReceiveRepository.save(entity);
    }

    @Transactional
    public List<ProductReceiveEntity> saveListAndModify(List<ProductReceiveEntity> entities) {
        return productReceiveRepository.saveAll(entities);
    }

    public void destroyOne(Integer productReceiveCid) {
        productReceiveRepository.findById(productReceiveCid).ifPresent(receive -> {
            productReceiveRepository.delete(receive);
        });
    }

    public void bulkInsert(List<ProductReceiveEntity> entities){
        productReceiveCustomJdbc.jdbcBulkInsert(entities);
    }

    public void deleteByErpOrderItemIds(List<UUID> erpOrderItemIds){
        productReceiveRepository.deleteByErpOrderItemIds(erpOrderItemIds);
    }

    public List<ProductReceiveProjection.RelatedProductAndProductOption> qSearchBatchByOptionIds(List<UUID> optionIds, Map<String, Object> params) {
        return productReceiveRepository.qSearchBatchByOptionIds(optionIds, params);
    }
}
