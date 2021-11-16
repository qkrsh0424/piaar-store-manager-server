package com.piaar_store_manager.server.model.user.dto;

import java.util.UUID;

import com.piaar_store_manager.server.model.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGetDto {
    private UUID id;
    private String username;
    private String roles;
    private String name;

    public static UserGetDto toDto(UserEntity entity){
        UserGetDto dto = UserGetDto.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .roles(entity.getRoles())
            .name(entity.getName())
        .build();

        return dto;
    }
}
