package com.example.zhong.starter.util;



import com.example.zhong.starter.R;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    public static final String address="http://193.112.44.141:8000";
    private static OkHttpClient okHttpClient;
    static {
        okHttpClient=new OkHttpClient();
    }
    public static void sendGet(String location, Callback callback){
        Request request=new Request.Builder()
                    .url(address+location)
                    .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void sendPost(String location, RequestBody requestBody, Callback callback){
        Request request=new Request.Builder()
                .url(address+location)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
