package com.piaar_store_manager.server.domain.account_book.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.piaar_store_manager.server.domain.account_book.dto.AccountBookDto;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "account_book")
public class AccountBookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_book_pk")
    private Long accountBookPk;
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;
    @Type(type = "uuid-char")
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "account_book_type")
    private String accountBookType;
    @Column(name = "bank_type")
    private String bankType;
    @Column(name = "\"desc\"")
    private String desc;
    @Column(name = "money")
    private Long money;
    @Column(name = "expenditure_type_id")
    private Integer expenditureTypeId;
    @Column(name = "reg_date")
    private LocalDateTime regDate;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted")
    private Integer deleted;

    public static AccountBookEntity toEntity(AccountBookDto dto) {
        AccountBookEntity entity = AccountBookEntity.builder()
            .id(dto.getId())
            .userId(dto.getUserId())
            .accountBookType(dto.getAccountBookType())
            .bankType(dto.getBankType())
            .desc(dto.getDesc())
            .money(dto.getMoney())
            .expenditureTypeId(dto.getExpenditureTypeId())
            .regDate(dto.getRegDate())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .deleted(0)
            .build();

        return entity;
    }
}
