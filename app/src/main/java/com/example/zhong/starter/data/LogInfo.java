package com.example.zhong.starter.data;

import com.example.zhong.starter.vo.User;

public class LogInfo {
    private static User user;

    public static User getUser(){
        return user;
    }

    public static void setUser(User u){
        user=u;
    }
}
