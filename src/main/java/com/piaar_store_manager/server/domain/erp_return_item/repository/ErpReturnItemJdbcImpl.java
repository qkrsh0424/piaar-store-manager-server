package com.piaar_store_manager.server.domain.erp_return_item.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.erp_return_item.entity.ErpReturnItemEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ErpReturnItemJdbcImpl implements ErpReturnItemCustomJdbc {
    private final JdbcTemplate jdbcTemplate;
    private int batchSize = 300;

    @Override
    public void jdbcBulkInsert(List<ErpReturnItemEntity> entities) {
        int batchCount = 0;
        List<ErpReturnItemEntity> subItems = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            subItems.add(entities.get(i));
            if ((i + 1) % batchSize == 0) {
                batchCount = batchInsert(batchCount, subItems);
            }
        }
        if (!subItems.isEmpty()) {
            batchCount = batchInsert(batchCount, subItems);
        }
    }
    
    private int batchInsert(int batchCount, List<ErpReturnItemEntity> subItems) {
        String sql = "INSERT INTO erp_return_item" + 
                "(cid, id, waybill_number, courier, transport_type, delivery_charge_return_type, delivery_charge_return_yn, receive_location, return_reason_type, return_reason_detail, " + 
                "management_memo1, management_memo2, management_memo3, management_memo4, management_memo5, created_at, created_by, collect_yn, collect_at, collect_complete_yn, " + 
                "collect_complete_at, return_complete_yn, return_complete_at, return_reject_yn, return_reject_at, defective_yn, stock_reflect_yn, erp_order_item_id)" +
                "VALUES" + 
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                " ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ErpReturnItemEntity entity = subItems.get(i);
                ps.setObject(1, entity.getCid());
                ps.setObject(2, entity.getId().toString());
                ps.setObject(3, entity.getWaybillNumber());
                ps.setObject(4, entity.getCourier());
                ps.setObject(5, entity.getTransportType());
                ps.setObject(6, entity.getDeliveryChargeReturnType());
                ps.setObject(7, entity.getDeliveryChargeReturnYn());
                ps.setObject(8, entity.getReceiveLocation());
                ps.setObject(9, entity.getReturnReasonType());
                ps.setObject(10, entity.getReturnReasonDetail());
                ps.setObject(11, entity.getManagementMemo1());
                ps.setObject(12, entity.getManagementMemo2());
                ps.setObject(13, entity.getManagementMemo3());
                ps.setObject(14, entity.getManagementMemo4());
                ps.setObject(15, entity.getManagementMemo5());
                ps.setObject(16, entity.getCreatedAt());
                ps.setObject(17, entity.getCreatedBy().toString());
                ps.setObject(18, entity.getCollectYn());
                ps.setObject(19, entity.getCollectAt());
                ps.setObject(20, entity.getCollectCompleteYn());
                ps.setObject(21, entity.getCollectCompleteAt());
                ps.setObject(22, entity.getReturnCompleteYn());
                ps.setObject(23, entity.getReturnCompleteAt());
                ps.setObject(24, entity.getReturnRejectYn());
                ps.setObject(25, entity.getReturnRejectAt());
                ps.setObject(26, entity.getDefectiveYn());
                ps.setObject(27, entity.getStockReflectYn());
                ps.setObject(28, entity.getErpOrderItemId().toString());
            }

            @Override
            public int getBatchSize() {
                return subItems.size();
            }
        });

        subItems.clear();
        batchCount++;
        return batchCount;
    }
}
