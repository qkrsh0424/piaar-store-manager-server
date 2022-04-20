package com.piaar_store_manager.server.config.interceptor;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.annotation.RequiredRoleManager;
import com.piaar_store_manager.server.exception.CustomAccessDeniedException;
import com.piaar_store_manager.server.exception.CustomInvalidUserException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequiredRoleManagerInterceptor  implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod == false) {
            return false;
        }

        HandlerMethod method = (HandlerMethod) handler;

        RequiredRoleManager requiredRoleManager = method.getMethodAnnotation(RequiredRoleManager.class);

        if(requiredRoleManager == null) {
            return true;
        }

        if(!(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(r->r.getAuthority().equals("ROLE_ADMIN") ||
                r.getAuthority().equals("ROLE_MANAGER")))){
            throw new CustomAccessDeniedException("권한이 필요한 서비스입니다.");
        } else {
            return true;
        }
    }
}
