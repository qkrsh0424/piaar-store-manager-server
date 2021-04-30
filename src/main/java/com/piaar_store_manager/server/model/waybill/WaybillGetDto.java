package com.piaar_store_manager.server.model.waybill;

import java.util.List;

import lombok.Data;

@Data
public class WaybillGetDto {
    private String uuid;
    private String prodName;
    private List<WaybillAssembledDto> list;
}
