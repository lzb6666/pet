package com.example.zhong.starter.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.zhong.starter.util.SharedPreferencesUtil;
import com.example.zhong.starter.vo.User;

public class LogInfo {
    private static User user;
    static private SharedPreferences sharedPreferences;


    private LogInfo(Context context){

        sharedPreferences = context.getSharedPreferences("LogInfo", Context.MODE_PRIVATE);
    }


    public static User getUser(Context context){
        if(user==null)
            user = SharedPreferencesUtil.getUser(context);
        return user;
    }

    public static void setUser(Context context,User u){
        SharedPreferencesUtil.putUser(context,u);
        user=u;
    }


}
