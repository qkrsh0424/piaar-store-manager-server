package com.piaar_store_manager.server.domain.return_product_image.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.piaar_store_manager.server.domain.return_product_image.entity.ReturnProductImageEntity;

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
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProductImageDto {
    private UUID id;
    private String imageUrl;

    @NotBlank(message = "'이미지 파일명'을 입력해주세요.")
    @Size(max = 100, message = "'이미지 파일명'은 100자 이내로 입력해주세요.")
    private String imageFileName;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private UUID productOptionId;
    private UUID erpReturnItemId;

    public static ReturnProductImageDto toDto(ReturnProductImageEntity entity) {
        ReturnProductImageDto dto = ReturnProductImageDto.builder()
            .id(entity.getId())
            .imageUrl(entity.getImageUrl())
            .imageFileName(entity.getImageFileName())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .productOptionId(entity.getProductOptionId())
            .erpReturnItemId(entity.getErpReturnItemId())
            .build();

        return dto;
    }
}
