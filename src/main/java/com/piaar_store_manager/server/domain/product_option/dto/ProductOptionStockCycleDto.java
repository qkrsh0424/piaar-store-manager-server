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
    UUID productId;
    Integer stockForW1;
    Integer releaseForW1;
    Integer receiveForW1;

    Integer releaseForW2;
    Integer receiveForW2;

    Integer releaseForW3;
    Integer receiveForW3;

    Integer releaseForW4;
    Integer receiveForW4;

    Integer releaseForW5;
    Integer receiveForW5;

    Integer releaseForW6;
    Integer receiveForW6;

    Integer releaseForW7;
    Integer receiveForW7;

    Integer releaseForW8;
    Integer receiveForW8;

    Integer releaseForW9;
    Integer receiveForW9;

    Integer releaseForW10;
    Integer receiveForW10;

    Integer releaseForW11;
    Integer receiveForW11;

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
                .productId(UUID.fromString(rs.getString("productId")))
                .stockForW1(rs.getInt("stockForW1"))
                .releaseForW1(rs.getInt("releaseForW1"))
                .receiveForW1(rs.getInt("receiveForW1"))
                .releaseForW2(rs.getInt("releaseForW2"))
                .receiveForW2(rs.getInt("receiveForW2"))
                .releaseForW3(rs.getInt("releaseForW3"))
                .receiveForW3(rs.getInt("receiveForW3"))
                .releaseForW4(rs.getInt("releaseForW4"))
                .receiveForW4(rs.getInt("receiveForW4"))
                .releaseForW5(rs.getInt("releaseForW5"))
                .receiveForW5(rs.getInt("receiveForW5"))
                .releaseForW6(rs.getInt("releaseForW6"))
                .receiveForW6(rs.getInt("receiveForW6"))
                .releaseForW7(rs.getInt("releaseForW7"))
                .receiveForW7(rs.getInt("receiveForW7"))
                .releaseForW8(rs.getInt("releaseForW8"))
                .receiveForW8(rs.getInt("receiveForW8"))
                .releaseForW9(rs.getInt("releaseForW9"))
                .receiveForW9(rs.getInt("receiveForW9"))
                .releaseForW10(rs.getInt("releaseForW10"))
                .receiveForW10(rs.getInt("receiveForW10"))
                .releaseForW11(rs.getInt("releaseForW11"))
                .receiveForW11(rs.getInt("receiveForW11"))
                .build();

            return dto;
        }
    }
}
