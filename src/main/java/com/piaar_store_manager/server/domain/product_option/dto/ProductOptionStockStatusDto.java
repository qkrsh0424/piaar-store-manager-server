package com.piaar_store_manager.server.domain.product_option.dto;

import java.util.List;

import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionStockStatusDto {
    private List<ProductReleaseGetDto> productRelease;
    private List<ProductReceiveGetDto> productReceive;

    /**
     * 모든 option 조회,
     * option에 대응되는 receive, receive 데이터에 대응되는 option, product를 포함
     * option에 대응되는 release, release 데이터에 대응되는 option, product를 포함
     */
    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinReceiveAndRelease {
        List<ProductReceiveGetDto.JoinProdAndOption> productReceive;
        List<ProductReleaseGetDto.JoinProdAndOption> productRelease;
    }
}
