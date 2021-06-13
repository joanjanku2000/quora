package com.ikub.intern.forum.Quora.utils;


import com.ikub.intern.forum.Quora.dto.user.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.http.HttpSession;

public class LoggedUserUtil {
        public static OAuth2User getLoggedUser(){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            return (OAuth2User) authentication.getPrincipal();
        }
        public static UserDto getLoggedUserDto(HttpSession httpSession) {
        return (UserDto) httpSession.getAttribute("loggedUser");
        }
}
