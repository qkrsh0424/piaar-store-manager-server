package com.piaar_store_manager.server.model.product_option.dto;

import java.util.List;

import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveJoinOptionDto;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseJoinOptionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionJoinReceiveAndReleaseDto {
    private List<ProductReleaseJoinOptionDto> productRelease;
    private List<ProductReceiveJoinOptionDto> productReceive;
}
