package com.piaar_store_manager.server.config.interceptor;

import com.piaar_store_manager.server.annotation.PermissionRole;
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

        if(!(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(r->r.getAuthority().equals(permissionRole.role()) ||
                r.getAuthority().equals(permissionRole.role())))){
            throw new CustomAccessDeniedException("권한이 필요한 서비스입니다.");
        } else {
            return true;
        }
    }
}
