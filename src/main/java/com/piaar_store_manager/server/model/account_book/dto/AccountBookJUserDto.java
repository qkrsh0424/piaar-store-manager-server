package com.piaar_store_manager.server.model.account_book.dto;

import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.model.user.entity.UserEntity;

import lombok.Data;

@Data
public class AccountBookJUserDto {
    private UUID id;
    private UUID userId;
    private String accountBookType;
    private String bankType;
    private String desc;
    private Long money;
    private String expenditureType;
    private Date regDate;
    private Date createdAt;
    private Date updatedAt;
    private String name;

    public AccountBookJUserDto(AccountBookEntity accountBook, UserEntity user){
        this.id = accountBook.getId();
        this.userId = accountBook.getUserId();
        this.accountBookType = accountBook.getAccountBookType();
        this.bankType = accountBook.getBankType();
        this.desc = accountBook.getDesc();
        this.money = accountBook.getMoney();
        this.expenditureType = accountBook.getExpenditureType();
        this.regDate = accountBook.getRegDate();
        this.createdAt = accountBook.getCreatedAt();
        this.updatedAt = accountBook.getUpdatedAt();
        this.name = user !=null ? user.getName() : "";
    }

    // public UUID getId(){
    //     return this.accountBook.getId();
    // }

    // public UUID getUserId(){
    //     return this.accountBook.getUserId();
    // }

    // public String getAccountBookType(){
    //     return this.accountBook.getAccountBookType();
    // }
    // public String getBankType(){
    //     return this.accountBook.getBankType();
    // }
    // public String getDesc(){
    //     return this.accountBook.getDesc();
    // }
    // public Long getMoney(){
    //     return this.accountBook.getMoney();
    // }
    // public String getExpenditureType(){
    //     return this.accountBook.getExpenditureType();
    // }
    // public Date getRegDate(){
    //     return this.accountBook.getRegDate();
    // }
    // public Date getCreatedAt(){
    //     return this.accountBook.getCreatedAt();
    // }
    // public Date getUpdatedAt(){
    //     return this.accountBook.getUpdatedAt();
    // }
    // public String getName(){
    //     return this.user !=null ? this.user.getName() : "";
    // }

    // @Override
    // public String toString() {
    //     // TODO Auto-generated method stub
    //     return "AccountBookReqDto [id="+this.getId()+","+"userId="+this.getUserId()+","+"accountBookType="+this.getAccountBookType()+","+"bankType="+this.getBankType()+","+"desc="+this.getDesc()+","+"money="+this.getMoney()+","+"regDate="+this.getRegDate()+","+"createdAt="+this.getCreatedAt()+","+"updatedAt="+this.getUpdatedAt()+","+"name="+this.getName()+"]";
    // }
}
