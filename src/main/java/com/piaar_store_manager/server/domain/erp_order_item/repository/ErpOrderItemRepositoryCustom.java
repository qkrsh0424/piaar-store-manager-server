package com.piaar_store_manager.server.domain.erp_order_item.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_order_item.entity.ErpOrderItemEntity;
import com.piaar_store_manager.server.domain.erp_order_item.filter.PerformanceSearchFilter;
import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpOrderItemRepositoryCustom {
    List<ErpOrderItemEntity> qfindAllByIdList(List<UUID> idList);
    List<ErpOrderItemProj> qfindAllM2OJ(Map<String, Object> params);
    List<ErpOrderItemProj> qfindAllM2OJByReleaseItem(Map<String, Object> params);
    List<ErpOrderItemProj> qfindAllM2OJByIdList(List<UUID> idList, Map<String, Object> params);
    List<ErpOrderItemProj> qfindAllM2OJByReleasedItemIdList(List<UUID> idList, Map<String, Object> params);
    Page<ErpOrderItemProj> qfindAllM2OJByPage(Map<String, Object> params, Pageable pageable);
    Page<ErpOrderItemProj> qfindReleaseItemM2OJByPage(Map<String, Object> params, Pageable pageable);
    Page<ErpOrderItemProj> qfindSalesPerformanceByPage(PerformanceSearchFilter filter, Pageable pageable);
}
