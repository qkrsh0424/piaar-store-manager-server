package com.piaar_store_manager.server.domain.erp_return_item.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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

    @Size(max = 36, message = "'반품 운송장번호'는 36자 이내로 입력해주세요.")
    private String waybillNumber;

    @Size(max = 40, message = "'반품 택배사'는 40자 이내로 입력해주세요.")
    private String courier;
    
    @Size(max = 45, message = "'반품 배송방식'은 45자 이내로 입력해주세요.")
    private String transportType;
    
    @Size(max = 10, message = "'반품배송비 입금방식'는 10자 이내로 입력해주세요.")
    private String deliveryChargeReturnType;

    @Pattern(regexp = "^[n|y]$", message = "'반품배송비 입금여부'에 올바른 값을 입력해주세요.")
    @Size(min = 1, max = 1)
    private String deliveryChargeReturnYn;
    
    @Size(max = 50, message = "'반품 수거지'는 50자 이내로 입력해주세요.")
    private String receiveLocation;

    @NotBlank(message = "'반품 요청사유'를 입력해주세요.")
    @Size(max = 30, message = "'반품 요청사유'는 30자 이내로 입력해주세요.")
    private String returnReasonType;
    
    @Size(max = 300, message = "'반품 상세사유'는 300자 이내로 입력해주세요.")
    private String returnReasonDetail;

    @Size(max = 200, message = "'관리메모1'은 200자 이내로 입력해주세요.")
    private String managementMemo1;

    @Size(max = 200, message = "'관리메모2'는 200자 이내로 입력해주세요.")
    private String managementMemo2;

    @Size(max = 200, message = "'관리메모3'은 200자 이내로 입력해주세요.")
    private String managementMemo3;
    
    @Size(max = 200, message = "'관리메모4'는 200자 이내로 입력해주세요.")
    private String managementMemo4;

    @Size(max = 200, message = "'관리메모5'는 200자 이내로 입력해주세요.")
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
        @Valid
        ErpReturnItemDto erpReturnItemDto;
        @Valid
        List<ReturnProductImageDto> imageDtos;
    }
}
