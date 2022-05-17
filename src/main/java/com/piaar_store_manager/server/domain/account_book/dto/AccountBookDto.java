package com.piaar_store_manager.server.domain.account_book.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.domain.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.domain.account_book.proj.AccountBookJoinProj;
import com.piaar_store_manager.server.domain.expenditure_type.dto.ExpenditureTypeDto;
import com.piaar_store_manager.server.domain.user.dto.UserGetDto;

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
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AccountBookDto {
    private UUID id;
    private UUID userId;
    private String accountBookType;
    private String bankType;
    private String desc;
    private Long money;
    private Integer expenditureTypeId;
    private LocalDateTime regDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AccountBookDto toDto(AccountBookEntity entity) {
        AccountBookDto dto = AccountBookDto.builder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .accountBookType(entity.getAccountBookType())
            .bankType(entity.getBankType())
            .desc(entity.getDesc())
            .money(entity.getMoney())
            .expenditureTypeId(entity.getExpenditureTypeId())
            .regDate(entity.getRegDate())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();

        return dto;
    }

    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDto {
        private AccountBookDto accountBook;
        private UserGetDto user;

        @Setter
        private ExpenditureTypeDto expenditureType;

        public static JoinDto toDto(AccountBookJoinProj proj) {
            JoinDto dto = JoinDto.builder()
                .accountBook(AccountBookDto.toDto(proj.getAccountBook()))
                .user(UserGetDto.toDto(proj.getUser()))
                .build();
            
            if(proj.getExpenditureType() != null) {
                dto.setExpenditureType(ExpenditureTypeDto.toDto(proj.getExpenditureType()));
            }

            return dto;
        }
    }
}
