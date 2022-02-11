package com.piaar_store_manager.server.model.delivery_ready.piaar.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PiaarItemDto {
    private UUID id;
    private Integer cellNumber;
    private Object cellValue;
}
