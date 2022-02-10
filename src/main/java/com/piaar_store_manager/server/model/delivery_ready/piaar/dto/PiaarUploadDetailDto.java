package com.piaar_store_manager.server.model.delivery_ready.piaar.dto;

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
public class PiaarUploadDetailDto {
    @Type(type = "jsonb")
    private List<PiaarItemDto> details;
}