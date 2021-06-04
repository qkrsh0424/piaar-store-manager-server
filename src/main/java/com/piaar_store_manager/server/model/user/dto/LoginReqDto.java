package com.piaar_store_manager.server.model.user.dto;

import lombok.Data;

@Data
public class LoginReqDto {
    private String username;
    private String password;
}
