package com.vihanga.eEducate.constant;

public class Authority {

    public static final String[] STUDENT_AUTHORITIES = { "user:read" , "user:delete"};
    public static final String[] TEACHER_AUTHORITIES = { "user:read" };
    public static final String[] CLASS_TEACHER_AUTHORITIES = { "user:read" , "user:create" , "user:update"};
    public static final String[] PRINCIPLE_AUTHORITIES = { "user:read" , "user:create" , "user:update" , "user:delete"};
    public static final String[] WEB_ADMIN_AUTHORITIES = { "user:read" , "user:create" , "user:update" , "user:delete"};

}
