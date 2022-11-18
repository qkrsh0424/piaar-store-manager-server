package com.piaar_store_manager.server.domain.product_receive.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductReceiveJdbcImpl implements ProductReceiveCustomJdbc {
    private final JdbcTemplate jdbcTemplate;
    private final int DEFAULT_BATCH_SIZE = 300;
    
    @Override
    public void jdbcBulkInsert(List<ProductReceiveEntity> entities) {
        int batchCount = 0;
        List<ProductReceiveEntity> subItems = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            subItems.add(entities.get(i));
            if((i+1) % DEFAULT_BATCH_SIZE == 0) {
                batchCount = batchInsert(DEFAULT_BATCH_SIZE, batchCount, subItems);
            }
        }

        if(!subItems.isEmpty()) {
            batchCount = batchInsert(DEFAULT_BATCH_SIZE, batchCount, subItems);
        }
    }

    private int batchInsert(int batchSize, int batchCount, List<ProductReceiveEntity> subItems){
        String sql = "INSERT INTO product_receive" +
                "(cid, id, receive_unit, memo, created_at, created_by, product_option_cid, product_option_id, erp_order_item_id)" +
                "VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ProductReceiveEntity entity = subItems.get(i);
                ps.setObject(1, entity.getCid());
                ps.setObject(2, entity.getId().toString());
                ps.setInt(3, entity.getReceiveUnit());
                ps.setString(4, entity.getMemo());
                ps.setObject(5, entity.getCreatedAt());
                ps.setObject(6, entity.getCreatedBy().toString());
                ps.setInt(7, entity.getProductOptionCid());
                ps.setObject(8, entity.getProductOptionId().toString());
                ps.setObject(9, entity.getErpOrderItemId() != null ? entity.getErpOrderItemId().toString() : null);
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
