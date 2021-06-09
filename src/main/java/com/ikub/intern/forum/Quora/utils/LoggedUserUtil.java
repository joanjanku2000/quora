package com.ikub.intern.forum.Quora.utils;

import com.ikub.intern.forum.Quora.dto.LoggedUser;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import com.ikub.intern.forum.Quora.security.oauth.CustomOauth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;


public class LoggedUserUtil {
        public static OAuth2User getLoggedUser(){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (OAuth2User) authentication.getPrincipal();
        }
}
