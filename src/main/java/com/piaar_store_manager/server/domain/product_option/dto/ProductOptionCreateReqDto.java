package com.piaar_store_manager.server.domain.product_option.dto;

import java.util.List;

import com.piaar_store_manager.server.model.option_package.dto.OptionPackageDto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductOptionCreateReqDto {
    ProductOptionGetDto optionDto;
    private List<OptionPackageDto> packageDtos;
}
