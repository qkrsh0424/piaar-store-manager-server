package com.piaar_store_manager.server.domain.erp_return_item.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.piaar_store_manager.server.domain.return_product_image.dto.ReturnProductImageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter @Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErpReturnItemDto {
    private UUID id;

    @Size(max = 36)
    private String waybillNumber;

    @Size(max = 40)
    private String courier;
    
    @Size(max = 45)
    private String transportType;
    
    @Size(max = 10)
    private String deliveryChargeReturnType;

    private String deliveryChargeReturnYn;
    
    @Size(max = 50)
    private String receiveLocation;

    @NotNull
    @NotBlank
    @Size(max = 30)
    private String returnReasonType;

    @Size(max = 300)
    private String returnReasonDetail;

    @Size(max = 200)
    private String managementMemo1;

    @Size(max = 200)
    private String managementMemo2;

    @Size(max = 200)
    private String managementMemo3;
    
    @Size(max = 200)
    private String managementMemo4;

    @Size(max = 200)
    private String managementMemo5;

    private LocalDateTime createdAt;
    private UUID createdBy;
    private String collectYn;
    private LocalDateTime collectAt;
    private String collectCompleteYn;
    private LocalDateTime collectCompleteAt;
    private String returnCompleteYn;
    private LocalDateTime returnCompleteAt;
    private String returnRejectYn;
    private LocalDateTime returnRejectAt;
    private String defectiveYn;
    private String stockReflectYn;
    private UUID erpOrderItemId;

    /**
     * 반품 데이터 & 반품 상품 이미지 생성 시 넘어오는 객체. erp return item. 그 하위 데이터 return product image로 구성된 객체
     */
    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReq {
        ErpReturnItemDto erpReturnItemDto;
        List<ReturnProductImageDto> imageDtos;
    }
}
