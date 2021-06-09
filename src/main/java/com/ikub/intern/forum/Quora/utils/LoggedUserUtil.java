package com.ikub.intern.forum.Quora.utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class LoggedUserUtil {
        public static OAuth2User getLoggedUser(){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (OAuth2User) authentication.getPrincipal();
        }
}
