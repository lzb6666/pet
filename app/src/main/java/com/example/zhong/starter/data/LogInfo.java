package com.example.zhong.starter.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.zhong.starter.util.SharedPreferencesUtil;
import com.example.zhong.starter.vo.User;

public class LogInfo {
    private static User user;
    static private LogInfo logInfo = null;
    static private SharedPreferences sharedPreferences;


    private LogInfo(Context context){
        sharedPreferences = context.getSharedPreferences("LogInfo", Context.MODE_PRIVATE);
    }

    static public LogInfo getInstance(Context context) {
        if(logInfo == null)
            logInfo = new LogInfo(context);
        return logInfo;
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean("isLogin",false);
    }

    public LogInfo login(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", true);
        editor.apply();
        return logInfo;
    }

    public LogInfo logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", false);
        editor.apply();
        return logInfo;
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
