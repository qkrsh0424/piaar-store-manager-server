package com.piaar_store_manager.server.domain.product_release.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductReleaseJdbcImpl implements ProductReleaseCustomJdbc{
    private final JdbcTemplate jdbcTemplate;
    private final int DEFAULT_BATCH_SIZE = 300;

    @Override
    public void jdbcBulkInsert(List<ProductReleaseEntity> entities){
        int batchCount = 0;
        List<ProductReleaseEntity> subItems = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            subItems.add(entities.get(i));
            if ((i + 1) % DEFAULT_BATCH_SIZE == 0) {
                batchCount = batchInsert(DEFAULT_BATCH_SIZE, batchCount, subItems);
            }
        }
        if (!subItems.isEmpty()) {
            batchCount = batchInsert(DEFAULT_BATCH_SIZE, batchCount, subItems);
        }
//        log.info("batchCount: " + batchCount);
    }

    private int batchInsert(int batchSize, int batchCount, List<ProductReleaseEntity> subItems){
        String sql = "INSERT INTO product_release" +
                "(cid, id, release_unit, memo, created_at, created_by, product_option_cid, product_option_id, erp_order_item_id)" +
                "VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ProductReleaseEntity entity = subItems.get(i);
                ps.setObject(1, entity.getCid());
                ps.setObject(2, entity.getId().toString());
                ps.setInt(3, entity.getReleaseUnit());
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
