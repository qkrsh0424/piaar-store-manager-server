package com.piaar_store_manager.server.domain.erp_order_item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErpReleaseConfirmItemDto {
    private String code;
    private String prodDefaultName;
    private String optionDefaultName;
    private Integer unit;
    private Integer optionStockUnit;
    private String optionPackageYn;
    private String parentOptionCode;
}
