package com.piaar_store_manager.server.model.product_option.dto;

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
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveReleaseSumOnlyDto {
    private Integer optionCid;
    private Integer receivedSum;
    private Integer releasedSum;
}
