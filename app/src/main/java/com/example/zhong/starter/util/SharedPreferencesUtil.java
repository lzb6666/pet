package com.example.zhong.starter.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.zhong.starter.vo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class SharedPreferencesUtil {

    private static SharedPreferences sp;

    private static final String spFileName = "welcomePage";
    public static final String FIRST_OPEN = "first_open";

    public static Boolean getBoolean(Context context, String strKey,
                                     Boolean strDefault) {//strDefault	boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, MODE_PRIVATE);
        return setPreferences.getBoolean(strKey, strDefault);
    }

    public static void putBoolean(Context context, String strKey,
                                  Boolean strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.apply();
    }

    public static void putUser(Context ctx, User user) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("LogInfo", MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("userInfo", json);
        editor.commit();
    }

    public static User getUser(Context ctx) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("LogInfo", MODE_PRIVATE);
        }
        Gson gson = new Gson();
        String json = sp.getString("userInfo", null);
        Type type = new TypeToken<User>() {
        }.getType();
        User user = gson.fromJson(json, type);
        return user;
    }



}
