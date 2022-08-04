package com.piaar_store_manager.server.domain.product_option.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionStockCycleDto {   // w1: 최근 1주, w2: w1 전 1주, w3: w3 전 1주, ...
    Integer optionCid;
    UUID optionId;
    Integer totalStockUnitForW1;
    Integer releaseSumUnitForW1;
    Integer receiveSumUnitForW1;
    Integer releaseSumUnitForW2;
    Integer receiveSumUnitForW2;
    Integer releaseSumUnitForW3;
    Integer receiveSumUnitForW3;
    Integer releaseSumUnitForW4;
    Integer receiveSumUnitForW4;
    Integer releaseSumUnitForW5;
    Integer receiveSumUnitForW5;
    Integer releaseSumUnitForW6;
    Integer receiveSumUnitForW6;
    Integer releaseSumUnitForW7;
    Integer receiveSumUnitForW7;

    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    public static class Mapper implements RowMapper<ProductOptionStockCycleDto> {
        
        @Override
        public ProductOptionStockCycleDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductOptionStockCycleDto dto = ProductOptionStockCycleDto.builder()
                .optionCid(rs.getInt("optionCid"))
                .optionId(UUID.fromString(rs.getString("optionId")))
                .totalStockUnitForW1(rs.getInt("totalStockUnitForW1"))
                .releaseSumUnitForW1(rs.getInt("releaseSumUnitForW1"))
                .receiveSumUnitForW1(rs.getInt("receiveSumUnitForW1"))
                .releaseSumUnitForW2(rs.getInt("releaseSumUnitForW2"))
                .receiveSumUnitForW2(rs.getInt("receiveSumUnitForW2"))
                .releaseSumUnitForW3(rs.getInt("releaseSumUnitForW3"))
                .receiveSumUnitForW3(rs.getInt("receiveSumUnitForW3"))
                .releaseSumUnitForW4(rs.getInt("releaseSumUnitForW4"))
                .receiveSumUnitForW4(rs.getInt("receiveSumUnitForW4"))
                .releaseSumUnitForW5(rs.getInt("releaseSumUnitForW5"))
                .receiveSumUnitForW5(rs.getInt("receiveSumUnitForW5"))
                .releaseSumUnitForW6(rs.getInt("releaseSumUnitForW6"))
                .receiveSumUnitForW6(rs.getInt("receiveSumUnitForW6"))
                .releaseSumUnitForW7(rs.getInt("releaseSumUnitForW7"))
                .receiveSumUnitForW7(rs.getInt("receiveSumUnitForW7"))
                .build();

            return dto;
        }
    }
}
