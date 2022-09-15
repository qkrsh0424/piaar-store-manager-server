package com.piaar_store_manager.server.domain.product_option.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionStockCycleDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductOptionJdbcImpl implements ProductOptionCustomJdbc {
    private final JdbcTemplate jdbcTemplate;
    private final int TOTAL_WEEK = 7;

    @Override
    public List<ProductOptionStockCycleDto> searchStockStatusByWeek(LocalDateTime searchEndDate, Integer categoryCid) {
        List<ProductOptionStockCycleDto> cycleDtos = new ArrayList<>();
        List<String> searchParams = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        // 파라미터로 넘어온 searchEndDate값은 [YYYY-MM-DDT14:59:00]로 넘어온다
        LocalDateTime endDate = searchEndDate;
        LocalDateTime startDate = endDate.minusDays(7).plusMinutes(1);

        // 총 재고수량 추출
        sql.append("SELECT po.cid AS optionCid, po.id AS optionId, p.id AS productId, "
                    + "SUM( CASE WHEN s.created_at <= ? AND s.status= ? THEN (s.unit * -1) "
                    + "WHEN s.created_at <= ? AND s.status= ? THEN s.unit ELSE 0 "
                    + "END) AS stockForW1 ");
        
        searchParams.add(endDate.toString());
        searchParams.add("release");
        searchParams.add(endDate.toString());
        searchParams.add("receive");

        // 기간별(W) 출고 수량, 입고 수량 계산
        for(int i = 0; i < TOTAL_WEEK; i++) {
            sql.append(", SUM(CASE WHEN s.created_at BETWEEN ? AND ? "
                    + "AND s.status = ? THEN s.unit ELSE 0 END) AS ?, "
                    + "SUM(CASE WHEN s.created_at BETWEEN ? AND ? "
                    + "AND s.status = ? THEN s.unit ELSE 0 END) AS ? ");

            searchParams.add(startDate.toString());
            searchParams.add(endDate.toString());
            searchParams.add("release");
            searchParams.add("releaseForW" + (i+1));

            searchParams.add(startDate.toString());
            searchParams.add(endDate.toString());
            searchParams.add("receive");
            searchParams.add("receiveForW" + (i+1));

            endDate = startDate.minusMinutes(1);
            startDate = endDate.minusDays(7).plusMinutes(1);
        }

        // 옵션별 product_release, product_receive 추출
        sql.append( "FROM product_option po "
            + "INNER JOIN product p ON p.cid=po.product_cid "
            + "LEFT JOIN ("
            + "SELECT 'release' as status, release_unit as unit, created_at as created_at, product_option_cid as product_option_cid "
            + "FROM product_release "
            + "UNION ALL "
            + "SELECT 'receive' as status, receive_unit as unit, created_at as created_at, product_option_cid as product_option_cid "
            + "FROM product_receive "
            + ") as s ON s.product_option_cid = po.cid "
            + "WHERE p.product_category_cid= ? "
            + "GROUP BY po.cid"
        );

        searchParams.add(categoryCid.toString());

        Object[] objs = new Object[searchParams.size()];
        objs = searchParams.stream().toArray();

        cycleDtos = jdbcTemplate.query(sql.toString(), new ProductOptionStockCycleDto.Mapper(), objs);
        return cycleDtos;
    }
}
