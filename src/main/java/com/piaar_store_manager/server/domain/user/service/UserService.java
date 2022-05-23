package com.piaar_store_manager.server.domain.user.service;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.config.auth.PrincipalDetails;
import com.piaar_store_manager.server.domain.message.Message;
import com.piaar_store_manager.server.domain.user.dto.SignupReqDto;
import com.piaar_store_manager.server.domain.user.dto.UserGetDto;
import com.piaar_store_manager.server.domain.user.entity.UserEntity;
import com.piaar_store_manager.server.domain.user.repository.UserRepository;
import com.piaar_store_manager.server.exception.CustomAccessDeniedException;
import com.piaar_store_manager.server.exception.CustomInvalidUserException;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final Integer DEFAULT_ALLOWED_ACCESS_COUNT = 3;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

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
        userEntity.setUpdatedAt(CustomDateUtils.getCurrentDateTime());
        userEntity.setCreatedAt(CustomDateUtils.getCurrentDateTime());

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
     * 슈퍼 관리자 권한인지 체크한다.
     * @return Boolean : if admin then true / else false
     */
    public boolean isSuperAdmin(){
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_SUPERADMIN"))){
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

    public void userLoginCheck() {
        if (!this.isUserLogin()) {
            throw new CustomInvalidUserException("로그인이 필요한 서비스 입니다.");
        }
    }

    public void userManagerRoleCheck() {
        if (!this.isManager()) {
            throw new CustomAccessDeniedException("접근 권한이 없습니다.");
        }
    }
}
