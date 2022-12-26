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
@Accessors(chain = true)
public class SalesChannelPerformanceProjectionV2 {
    private String datetime;
    @Setter
    private List<Performance> performance;

    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Performance {
        private String datetime;
        private String salesChannel;
        private Integer orderPayAmount;
        private Integer salesPayAmount;
        private Integer orderRegistration;
        private Integer salesRegistration;
        private Integer orderUnit;
        private Integer salesUnit;
    }
}
