package com.vihanga.eEducate.enumeration;

import com.vihanga.eEducate.constant.Authority;

public enum Role {

    ROLE_STUDENT(Authority.STUDENT_AUTHORITIES),
    ROLE_TEACHER(Authority.TEACHER_AUTHORITIES),
    ROLE_CLASS_TEACHER(Authority.CLASS_TEACHER_AUTHORITIES),
    ROLE_PRINCIPLE(Authority.PRINCIPLE_AUTHORITIES),
    ROLE_WEB_ADMIN(Authority.WEB_ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities){
        this.authorities = authorities;
    }

    public String[] getAuthorities(){
        return authorities;
    }
}
