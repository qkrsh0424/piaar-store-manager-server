package com.piaar_store_manager.server.service.user;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.config.auth.PrincipalDetails;
import com.piaar_store_manager.server.handler.DateHandler;
import com.piaar_store_manager.server.model.message.Message;
import com.piaar_store_manager.server.model.user.dto.SignupReqDto;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;
import com.piaar_store_manager.server.model.user.entity.UserEntity;
import com.piaar_store_manager.server.model.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     * <b>DB Insert Related Method<b/>
     * <p>
     * 유저 등록시 사용되는 서비스 메소드
     * @param signupReqDto
     * @return String : 
     *      if isDuplicatedUsername then "duplicated_username" else "success"
     * @see UserRepository
     */
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

    public UserGetDto getDtoByEntity(UserEntity userEntity){
        UserGetDto userGetDto = new UserGetDto();
        userGetDto.setId(userEntity.getId());
        userGetDto.setName(userEntity.getName());
        userGetDto.setRoles(userEntity.getRoles());
        userGetDto.setUsername(userEntity.getUsername());
        return userGetDto;
    }

    /**
     * 유저네임이 중복되는 데이터가 있는지 확인한다.
     * @param username
     * @return Boolean : if exist then true / else false
     */
    public boolean isDuplicatedUsername(String username){
        Optional<UserEntity> userEntityOpt = userRepository.findByUsername(username);
        if(userEntityOpt.isPresent()){
            return true;
        }
        
        return false;
    }

    /**
     * 관리자 권한인지 체크한다.
     * @return Boolean : if admin then true / else false
     */
    public boolean isAdmin(){
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_ADMIN"))){
            return true;
        }
        return false;
    }

    /**
     * 매니저 권한인지 체크한다.
     * @return Boolean : if admin | manager then true / else false
     */
    public boolean isManager(){
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_MANAGER"))){
            return true;
        }
        return false;
    }

    /**
     * 로그인 상태를 체크한다.
     * @return Boolean : if login then true / else false
     */
    public boolean isUserLogin(){
        if(SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")){
            return false;
        }
        return true;
    }

    /**
     * 유저아이디를 가져온다
     * @return UUID : userId
     */
    public UUID getUserId(){
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return principalDetails.getUser().getId();
    }

    /**
     * <b>Check the type of user denial.</b>
     */
    public void userDenyCheck(Message message) {

        if(!this.isUserLogin()){
            message.setMessage("need_login");
            message.setMemo("need login");
        }else{
            message.setMessage("access_denied");
            message.setMemo("access denied");
        }

        message.setStatus(HttpStatus.FORBIDDEN);
    }
}
