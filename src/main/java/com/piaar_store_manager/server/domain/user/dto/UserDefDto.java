package com.piaar_store_manager.server.domain.user.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class UserDefDto {
    private UUID id;
    private String username;
    private String password;
    private String roles;
    private String name;
    private Date updatedAt;
    private Date createdAt;
}
