package com.piaar_store_manager.server.config.interceptor;

import com.piaar_store_manager.server.annotation.PermissionRole;
import com.piaar_store_manager.server.config.auth.PrincipalDetails;
import com.piaar_store_manager.server.domain.user.entity.UserEntity;
import com.piaar_store_manager.server.exception.CustomAccessDeniedException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PermissionRoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod == false) {
            return false;
        }

        HandlerMethod method = (HandlerMethod) handler;

        PermissionRole permissionRole = method.getMethodAnnotation(PermissionRole.class);

        if(permissionRole == null) {
            return true;
        }

        PrincipalDetails principalDtails = (PrincipalDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = principalDtails.getUser();

        // permissionRole보다 높은 권한인지 확인
        if(!userEntity.hasPermissionRole(permissionRole.role())) {
            throw new CustomAccessDeniedException("권한이 필요한 서비스입니다.");
        } else {
            return true;
        }
    }
}
