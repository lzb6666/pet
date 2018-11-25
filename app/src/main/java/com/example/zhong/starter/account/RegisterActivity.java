package com.example.zhong.starter.account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zhong.starter.R;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.result.CodeResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity {
    private LinearLayout registerLayout;
    private EditText editTextPhoneNum;
    private EditText edtTxtVCode;
    private String phoneNum;
    private String VCode;

    private static final String TAG="RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextPhoneNum=this.findViewById(R.id.edtTxt_reg_phone);
        edtTxtVCode=this.findViewById(R.id.edtTxt_reg_vcode);
        registerLayout=this.findViewById(R.id.register);

        Button vcodeBtn=this.findViewById(R.id.btn_reg_getVcode);
        vcodeBtn.setOnClickListener((view)->{
            if (editTextPhoneNum.getText().length()==0){
                //TODO:验证手机号
            }else{
                phoneNum=editTextPhoneNum.getText().toString();
                this.getVCode(phoneNum);
            }
        });

        Button nextBtn=this.findViewById(R.id.btn_reg_next);
        nextBtn.setOnClickListener(view->{
                if (edtTxtVCode.getText().length()!=0){
                    phoneNum=editTextPhoneNum.getText().toString();
                    this.vrfVCode(phoneNum,edtTxtVCode.getText().toString());
                }
        });

        Button loginBtn=this.findViewById(R.id.btn_reg_login);
        loginBtn.setOnClickListener(v->{
            Intent intent=new Intent(this,LoginActivity.class);
            this.startActivity(intent);
            this.finish();
        });
    }

    private void getVCode(String phoneNum){
        RequestBody requestBody=new FormBody.Builder()
                .add("phoneNum",phoneNum)
                .add("type","1")
                .build();
        HttpUtil.sendPost("/account/VCode", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toast("服务器连接失败",Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CodeResult codeResult=JsonUtil.gson.fromJson(response.body().string(),CodeResult.class);
                Log.d(TAG, "onResponse: "+codeResult.getMsg());
                toast(codeResult.getMsg(), Toast.LENGTH_LONG);
            }
        });
    }

    private void vrfVCode(String phoneNum,String vcode){
        VCode=vcode;
        RequestBody requestBody=new FormBody.Builder()
                .add("phoneNum",phoneNum)
                .add("VCode",vcode)
                .add("type","1")
                .build();
        HttpUtil.sendPost("/account/vlfVCode", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toast("服务器连接失败",Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CodeResult codeResult=JsonUtil.gson.fromJson(response.body().string(),CodeResult.class);
                Log.d(TAG, "onResponse: "+codeResult.getMsg());
                if (codeResult.getRstCode()==200){
                    RegisterActivity.this.runOnUiThread(()->{
                        loadPsdContent();
                    });
                }
            }
        });
    }

    private void loadPsdContent(){
        registerLayout.removeAllViewsInLayout();

        View content = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.activity_register2, null);
        registerLayout.addView(content);

        final EditText editTextPassword1 = findViewById(R.id.editText_password1_card_points);
        final EditText editTextPassword2 =  findViewById(R.id.editText_password2_card_points);



        Button btnRegister = (Button) findViewById(R.id.button_register_card_points);
        btnRegister.setOnClickListener(v->{
                if (editTextPassword1.getText().length() == 0 || editTextPassword2.getText().length() == 0) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if ( ! editTextPassword1.getText().toString().equals(editTextPassword2.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                } else {
                    tryRegister(phoneNum, editTextPassword1.getText().toString());
                }
            });

        Button buttonLogin = findViewById(R.id.button_back_login_card_points);
        buttonLogin.setOnClickListener(v->{
                Intent intentToRegister = new Intent(this, LoginActivity.class);
                this.startActivity(intentToRegister);
                this.finish();
        });
    }

    private void tryRegister(String phoneNum,String pwd){
        RequestBody requestBody=new FormBody.Builder()
                .add("phoneNum",phoneNum)
                .add("VCode",VCode)
                .add("password",pwd)
                .build();
        HttpUtil.sendPost("/account/register", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toast("服务器连接失败",Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CodeResult codeResult=JsonUtil.gson.fromJson(response.body().string(),CodeResult.class);
                Log.d(TAG, "onResponse: "+codeResult.getMsg());
                if (codeResult.getRstCode()==200){
                    toast(codeResult.getMsg(), Toast.LENGTH_LONG);
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    RegisterActivity.this.startActivity(intent);
                    RegisterActivity.this.finish();
                }
            }
        });
    }

    private void toast(String msg,int time){
        this.runOnUiThread(()->{
            Toast.makeText(this,msg,time).show();
        });
    }
}
