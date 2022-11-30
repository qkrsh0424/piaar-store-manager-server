package com.piaar_store_manager.server.domain.return_product_image.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.return_product_image.entity.ReturnProductImageEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReturnProductImageJdbcImpl implements ReturnProductImageCustomJdbc {
    private final JdbcTemplate jdbcTemplate;
    private int batchSize = 300;

    @Override
    public void jdbcBulkInsert(List<ReturnProductImageEntity> entities) {
        int batchCount = 0;
        List<ReturnProductImageEntity> subItems = new ArrayList<>();
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

    private int batchInsert(int batchCount, List<ReturnProductImageEntity> subItems) {
        String sql = "INSERT INTO return_product_image" +
                "(cid, id, image_url, image_file_name, created_at, created_by, product_option_id, erp_return_item_id)" +
                "VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ReturnProductImageEntity entity = subItems.get(i);
                ps.setObject(1, entity.getCid());
                ps.setObject(2, entity.getId().toString());
                ps.setObject(3, entity.getImageUrl());
                ps.setObject(4, entity.getImageFileName());
                ps.setObject(5, entity.getCreatedAt());
                ps.setObject(6, entity.getCreatedBy().toString());
                ps.setObject(7, entity.getProductOptionId().toString());
                ps.setObject(8, entity.getErpReturnItemId().toString());
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
