package com.piaar_store_manager.server.model.account_book.dto;

import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.model.account_book.entity.AccountBookEntity;
import com.piaar_store_manager.server.model.user.entity.UserEntity;

import lombok.Data;

public class AccountBookJUserDto {
    private AccountBookEntity accountBook;
    private UserEntity user;

    public AccountBookJUserDto(AccountBookEntity accountBook, UserEntity user){
        this.accountBook = accountBook;
        this.user = user;
    }

    public UUID getId(){
        return this.accountBook.getId();
    }

    public UUID getUserId(){
        return this.accountBook.getUserId();
    }

    public String getAccountBookType(){
        return this.accountBook.getAccountBookType();
    }
    public String getBankType(){
        return this.accountBook.getBankType();
    }
    public String getDesc(){
        return this.accountBook.getDesc();
    }
    public Long getMoney(){
        return this.accountBook.getMoney();
    }
    public Date getRegDate(){
        return this.accountBook.getRegDate();
    }
    public Date getCreatedAt(){
        return this.accountBook.getCreatedAt();
    }
    public Date getUpdatedAt(){
        return this.accountBook.getUpdatedAt();
    }
    public String getName(){
        return this.user !=null ? this.user.getName() : "";
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "AccountBookReqDto [id="+this.getId()+","+"userId="+this.getUserId()+","+"accountBookType="+this.getAccountBookType()+","+"bankType="+this.getBankType()+","+"desc="+this.getDesc()+","+"money="+this.getMoney()+","+"regDate="+this.getRegDate()+","+"createdAt="+this.getCreatedAt()+","+"updatedAt="+this.getUpdatedAt()+","+"name="+this.getName()+"]";
    }
}
