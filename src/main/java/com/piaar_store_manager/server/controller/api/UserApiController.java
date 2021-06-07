package com.piaar_store_manager.server.controller.api;

import com.piaar_store_manager.server.config.auth.PrincipalDetails;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;
import com.piaar_store_manager.server.model.user.entity.UserEntity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserApiController {
    @GetMapping("/login-check")
    public ResponseEntity<?> loginCheckApi(){
        Message message = new Message();
        UserGetDto userGetDto = new UserGetDto();

        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            PrincipalDetails pd = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            UserEntity userEntity = pd.getUser();
            userGetDto.setId(userEntity.getId());
            userGetDto.setUsername(userEntity.getUsername());
            userGetDto.setRoles(userEntity.getRoles());
            
            message.setStatus(HttpStatus.OK);
            message.setMessage("loged");
            message.setMemo("already login");
            message.setData(userGetDto);
        } else {
            message.setStatus(HttpStatus.OK);
            message.setMessage("need_login");
            message.setMemo("need login");
        }
        return new ResponseEntity<>(message, message.getStatus());
    }
}
