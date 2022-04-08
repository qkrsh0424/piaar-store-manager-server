package com.piaar_store_manager.server.model.product.dto;

import com.piaar_store_manager.server.model.option_package.dto.OptionPackageDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ProductCreateReqDto {
    private ProductGetDto productDto;
    private List<ProductOptionGetDto> optionDtos;
    private List<OptionPackageDto> packageDtos;
}
