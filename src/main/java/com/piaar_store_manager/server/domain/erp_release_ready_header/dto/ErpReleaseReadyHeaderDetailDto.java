package com.piaar_store_manager.server.domain.erp_release_ready_header.dto;

import java.util.List;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpReleaseReadyHeaderDetailDto {
    @Type(type = "jsonb")
    private List<DetailDto> details;
}
