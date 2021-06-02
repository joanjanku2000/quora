package com.ikub.intern.forum.Quora.dto;

import com.ikub.intern.forum.Quora.entities.UserEntity;


public class LoggedUser {
    private static UserEntity userEntity ;

    public static void setLoggedUser(UserEntity userEntity) {
        LoggedUser.userEntity = userEntity;
    }

    public static UserEntity getLoggedUser(){
        return userEntity;
    }

}
