package com.piaar_store_manager.server.domain.sales_performance.proj;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

public class SalesPerformanceProjection {

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Dashboard {
        private String datetime;

        @Setter
        private Integer orderRegistration;
        @Setter
        private Integer orderPayAmount;
        @Setter
        private Integer salesRegistration;
        @Setter
        private Integer salesPayAmount;

        @Builder
        @NoArgsConstructor        
        public static class Mapper implements RowMapper<SalesPerformanceProjection.Dashboard> {
            
            @Override
            public Dashboard mapRow(ResultSet rs, int rowNum) throws SQLException {
                Dashboard proj = Dashboard.builder()
                    .datetime(rs.getString("datetime"))
                    .orderRegistration(rs.getInt("orderRegistration"))
                    .orderPayAmount(rs.getInt("orderPayAmount"))
                    .salesRegistration(rs.getInt("salesRegistration"))
                    .salesPayAmount(rs.getInt("salesPayAmount"))
                    .build();
                
                return proj;
            }
        }
    }

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class PayAmount {
        private String datetime;

        @Setter
        private Integer orderPayAmount;
        @Setter
        private Integer salesPayAmount;

        @Builder
        @NoArgsConstructor        
        public static class Mapper implements RowMapper<SalesPerformanceProjection.PayAmount> {
            
            @Override
            public PayAmount mapRow(ResultSet rs, int rowNum) throws SQLException {
                PayAmount proj = PayAmount.builder()
                    .datetime(rs.getString("datetime"))
                    .orderPayAmount(rs.getInt("orderPayAmount"))
                    .salesPayAmount(rs.getInt("salesPayAmount"))
                    .build();
                
                return proj;
            }
        }
    }

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class RegistrationAndUnit {
        private String datetime;

        @Setter
        private Integer orderRegistration;
        @Setter
        private Integer orderUnit;
        @Setter
        private Integer salesRegistration;
        @Setter
        private Integer salesUnit;
    }

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class SalesPayAmount {
        private String datetime;

        @Setter
        private Integer salesPayAmount;
    }

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class SummaryTable {
        private String datetime;

        @Setter
        private Integer orderRegistration;
        @Setter
        private Integer orderUnit;
        @Setter
        private Integer orderPayAmount;
        @Setter
        private Integer salesRegistration;
        @Setter
        private Integer salesUnit;
        @Setter
        private Integer salesPayAmount;
    }
}
