package com.piaar_store_manager.server.service.user;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.config.auth.PrincipalDetails;
import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.user.dto.SignupReqDto;
import com.piaar_store_manager.server.model.user.entity.UserEntity;
import com.piaar_store_manager.server.model.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    private final Integer DEFAULT_ALLOWED_ACCESS_COUNT = 3;
    
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DateHandler dateHandler;

    // Signup
    public String createOne(SignupReqDto signupReqDto){
        if(isDuplicatedUsername(signupReqDto.getUsername())){
            return "duplicated_username";
        }

        UserEntity userEntity = new UserEntity();
        String salt = UUID.randomUUID().toString();
        String password = encoder.encode(signupReqDto.getPassword() + salt);
        
        userEntity.setId(UUID.randomUUID());
        userEntity.setUsername(signupReqDto.getUsername());
        userEntity.setPassword(password);
        userEntity.setSalt(salt);
        userEntity.setName(signupReqDto.getName());
        userEntity.setRoles("ROLE_USER");
        userEntity.setAllowedAccessCount(DEFAULT_ALLOWED_ACCESS_COUNT);
        userEntity.setUpdatedAt(dateHandler.getCurrentDate());
        userEntity.setCreatedAt(dateHandler.getCurrentDate());

        log.info("UserService : createOne : print(userEntity) => {}.", userEntity);
        userRepository.save(userEntity);
        return "success";
    }

    public boolean isDuplicatedUsername(String username){
        Optional<UserEntity> userEntityOpt = userRepository.findByUsername(username);
        if(userEntityOpt.isPresent()){
            return true;
        }
        
        return false;
    }

    public boolean isAdmin(){
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_ADMIN"))){
            return true;
        }
        return false;
    }

    public boolean isManager(){
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_MANAGER"))){
            return true;
        }
        return false;
    }

    public boolean isUserLogin(){
        if(SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            return false;
        }
        return true;
    }

    public UUID getUserId(){
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return principalDetails.getUser().getId();
    }
}