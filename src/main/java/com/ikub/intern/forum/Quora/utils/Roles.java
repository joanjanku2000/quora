package com.ikub.intern.forum.Quora.utils;

public enum Roles {
    ADMIN("admin"),USER("user");
    public final String value;
    private Roles(String value){
        this.value = value;
    }
}
