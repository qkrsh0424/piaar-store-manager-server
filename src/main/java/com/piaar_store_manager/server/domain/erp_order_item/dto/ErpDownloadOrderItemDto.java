package com.piaar_store_manager.server.domain.erp_order_item.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;


@Builder
@Getter
@Setter
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErpDownloadOrderItemDto {
    private String receiver;
    private String destination;
    private String combinedUniqueCode;
    private String combinedFreightCode;
    private List<ErpOrderItemDto> collections;
}
