package com.piaar_store_manager.server.domain.analytics_page_view.naver.dto;

import lombok.Data;

@Data
public class NAPopularPageExcelFormDto {
    private String pageUrl;
    private Integer pageView;
    private String avgResidenceTime;
    private String ratio;
}
