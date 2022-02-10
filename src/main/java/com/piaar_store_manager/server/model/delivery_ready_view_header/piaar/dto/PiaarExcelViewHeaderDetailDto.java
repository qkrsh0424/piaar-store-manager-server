package com.piaar_store_manager.server.model.delivery_ready_view_header.piaar.dto;

import java.util.List;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PiaarExcelViewHeaderDetailDto {
    @Type(type = "jsonb")
    private List<ViewDetailDto> details;
}
