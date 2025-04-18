package com.example.springbootpractice.config.security;

import com.example.springbootpractice.exception.ApiErrorException;
import com.example.springbootpractice.exception.ErrorCode;
import com.example.springbootpractice.model.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication : {}", authentication);
        if (authentication == null) {
            throw new ApiErrorException(ErrorCode.AUTHENTICATION_FAIL);
        }
        if (!authentication.isAuthenticated()) {
            throw new ApiErrorException(ErrorCode.AUTHENTICATION_FAIL);
        }
        return authentication;
    }

    public static UserEntity getCurrentUser() {
        Authentication authentication = getAuthentication();
        SecurityUser springSecurityUser = (SecurityUser) authentication.getPrincipal();
        return springSecurityUser.getUser();
    }

    public static String getCurrentUserName() {
        return getCurrentUser().getName();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }

    public static long getCurrentUserSeq() {
        return getCurrentUser().getSeq();
    }

}
