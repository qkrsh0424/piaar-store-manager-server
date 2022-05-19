package com.piaar_store_manager.server.model.naver_analytics_page_view.dto;

import lombok.Data;

@Data
public class NAPopularPageExcelFormDto {
    private String pageUrl;
    private Integer pageView;
    private String avgResidenceTime;
    private String ratio;
}
