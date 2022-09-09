package com.piaar_store_manager.server.domain.erp_return_item.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.erp_return_item.proj.ErpReturnItemProj;

@Repository
public interface ErpReturnItemRepositoryCustom {
    Page<ErpReturnItemProj> qfindAllM2OJByPage(Map<String, Object> params, Pageable pageable);
    List<ErpReturnItemProj> qfindAllM2OJByReleasedItemIdList(List<UUID> idList, Map<String, Object> params);
}
