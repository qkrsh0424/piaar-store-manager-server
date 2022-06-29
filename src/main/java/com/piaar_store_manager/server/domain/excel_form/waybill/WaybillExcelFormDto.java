package com.piaar_store_manager.server.domain.excel_form.waybill;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaybillExcelFormDto {
    private String receiver;
    // private String freightCode;
    private String receiverContact1;
    private String waybillNumber;
    private String transportType;
    private String courier;
}
