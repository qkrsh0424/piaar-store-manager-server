package com.piaar_store_manager.server.domain.user.dto;

import lombok.Data;

@Data
public class SignupReqDto {
    private String username;
    private String password;
    private String name;
}
