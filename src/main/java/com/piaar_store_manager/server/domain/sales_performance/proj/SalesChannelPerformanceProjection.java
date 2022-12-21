package com.piaar_store_manager.server.domain.sales_performance.proj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SalesChannelPerformanceProjection {
    @Setter
    private String salesChannel;
    @Setter
    private Integer orderPayAmount;
    @Setter
    private Integer salesPayAmount;
    @Setter
    private Integer orderRegistration;
    @Setter
    private Integer salesRegistration;
    @Setter
    private Integer orderUnit;
    @Setter
    private Integer salesUnit;

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class PayAmount {
        private String datetime;
        @Setter
        private List<SalesChannelPerformanceProjection> performance;

        @Builder
        @NoArgsConstructor        
        public static class Mapper implements RowMapper<SalesChannelPerformanceProjection.PayAmount> {
            @Override
            public PayAmount mapRow(ResultSet rs, int rowNum) throws SQLException {
                SalesChannelPerformanceProjection salesChannelPerformance = SalesChannelPerformanceProjection.builder()
                    .salesChannel(rs.getString("salesChannel"))
                    .orderPayAmount(rs.getInt("orderPayAmount"))
                    .salesPayAmount(rs.getInt("salesPayAmount"))
                    .build();

                PayAmount proj = PayAmount.builder()
                    .datetime(rs.getString("datetime"))
                    .performance(Arrays.asList(salesChannelPerformance))
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
        private List<SalesChannelPerformanceProjection> performance;

        @Builder
        @NoArgsConstructor        
        public static class Mapper implements RowMapper<SalesChannelPerformanceProjection.RegistrationAndUnit> {
            @Override
            public RegistrationAndUnit mapRow(ResultSet rs, int rowNum) throws SQLException {
                SalesChannelPerformanceProjection salesChannelPerformance = SalesChannelPerformanceProjection.builder()
                    .salesChannel(rs.getString("salesChannel"))
                    .orderRegistration(rs.getInt("orderRegistration"))
                    .salesRegistration(rs.getInt("salesRegistration"))
                    .orderUnit(rs.getInt("orderUnit"))
                    .salesUnit(rs.getInt("salesUnit"))
                    .build();

                    RegistrationAndUnit proj = RegistrationAndUnit.builder()
                    .datetime(rs.getString("datetime"))
                    .performance(Arrays.asList(salesChannelPerformance))
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
    public static class SalesPayAmount {
        private String datetime;
        @Setter
        private List<SalesChannelPerformanceProjection> performance;

        @Builder
        @NoArgsConstructor        
        public static class Mapper implements RowMapper<SalesChannelPerformanceProjection.SalesPayAmount> {
            @Override
            public SalesPayAmount mapRow(ResultSet rs, int rowNum) throws SQLException {
                SalesChannelPerformanceProjection salesChannelPerformance = SalesChannelPerformanceProjection.builder()
                    .salesChannel(rs.getString("salesChannel"))
                    .salesPayAmount(rs.getInt("salesPayAmount"))
                    .build();

                SalesPayAmount proj = SalesPayAmount.builder()
                    .datetime(rs.getString("datetime"))
                    .performance(Arrays.asList(salesChannelPerformance))
                    .build();
                
                return proj;
            }
        }
    }
}
