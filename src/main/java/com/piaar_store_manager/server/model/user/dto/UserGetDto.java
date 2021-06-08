package com.piaar_store_manager.server.model.user.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class UserGetDto {
    private UUID id;
    private String username;
    private String roles;
    private String name;
}
