package com.piaar_store_manager.server.domain.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk")
    private Integer userPk;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name="salt")
    private String salt;

    @Column(name="name")
    private String name;

    @Column(name = "roles")
    private String roles;

    @Column(name = "allowed_access_count")
    private Integer allowedAccessCount;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public boolean hasPermissionRole(String permissionRole) {
        List<String> userRole = this.getRoleList();

        int allowedAccessLevel = 0;
        if(userRole.contains("ROLE_SUPERADMIN")){
            allowedAccessLevel = 4;
        }else if(userRole.contains("ROLE_ADMIN")) {
            allowedAccessLevel = 3;
        }else if(userRole.contains("ROLE_MANAGER")) {
            allowedAccessLevel = 2;
        }else if(userRole.contains("ROLE_USER")) {
            allowedAccessLevel = 1;
        }

        int roleAccessLevel = 0;
        switch(permissionRole) {
            case "ROLE_SUPERADMIN":
                roleAccessLevel = 4;
                break;
            case "ROLE_ADMIN":
                roleAccessLevel = 3;
                break;
            case "ROLE_MANAGER":
                roleAccessLevel = 2;
                break;
            case "ROLE_USER":
                roleAccessLevel = 1;
                break;
        }

        // if(allowedAccessLevel >= roleAccessLevel) {
        //     return true;
        // }else {
        //     return false;
        // }
        return allowedAccessLevel >= roleAccessLevel;
    }
}
