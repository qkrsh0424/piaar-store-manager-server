package com.piaar_store_manager.server.model.user.dto;

import lombok.Data;

@Data
public class SignupReqDto {
    private String username;
    private String password;
    private String name;
}
