package com.piaar_store_manager.server.model.product_option.dto;

import java.util.List;

import com.piaar_store_manager.server.model.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.model.product_release.dto.ProductReleaseGetDto;

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
public class ProductOptionStatusDto {
    List<ProductReleaseGetDto> productRelease;
    List<ProductReceiveGetDto> productReceive;
}
