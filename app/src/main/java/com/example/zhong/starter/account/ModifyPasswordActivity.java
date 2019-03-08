package com.example.zhong.starter.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhong.starter.R;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.main.MainActivity;
import com.example.zhong.starter.main.ModifyInfoActivity;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.TitleBar;
import com.example.zhong.starter.util.result.CodeResult;
import com.example.zhong.starter.vo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyPasswordActivity extends AppCompatActivity {

    private static final String TAG="ModifyPasswordActivity";
    private ProgressDialog dialog;

    private EditText editTextOldPassword;
    private EditText editTextNewPassword;
    private String strOldPassword;
    private String strNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        //设置toolbar
        toolBar();

        editTextOldPassword = findViewById(R.id.editText_old_password);
        editTextNewPassword = findViewById(R.id.editText_new_password);

        Button buttonLogin = (Button) findViewById(R.id.button_modify_password);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextOldPassword.getText().length() == 0) {
                    showAlert("请输入旧密码");
                } else if (editTextNewPassword.getText().length() == 0) {
                    showAlert("请输入新密码");
                } else {

                    strOldPassword = editTextOldPassword.getText().toString();
                    strNewPassword = editTextNewPassword.getText().toString();

                    tryModifyPassword();
                }
            }
        });
    }

    private void showAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModifyPasswordActivity.this);
        alertDialog.setTitle(msg).setPositiveButton("OK", null).show();
    }

    private void tryModifyPassword(){
        RequestBody requestBody=new FormBody.Builder()
                .add("userID",LogInfo.getUser(ModifyPasswordActivity.this).getUserID())
                .add("oldPassword",strOldPassword)
                .add("newPassword",strNewPassword)
                .build();
        HttpUtil.sendPost("/account/reset", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ModifyPasswordActivity.this.runOnUiThread(()->{
                    Toast.makeText(ModifyPasswordActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body()==null){
                    return;
                }
                CodeResult codeResult=JsonUtil.gson.fromJson(response.body().string(),CodeResult.class);

                Log.d(TAG, "onResponse: "+codeResult.getMsg());
                if (codeResult.getRstCode()==200){
                    ModifyPasswordActivity.this.runOnUiThread(()->{
                        Toast.makeText(ModifyPasswordActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                if (codeResult.getRstCode()==400){
                    Log.d(TAG, "onResponse: "+codeResult.getMsg());
                    ModifyPasswordActivity.this.runOnUiThread(()->{
                        Toast.makeText(ModifyPasswordActivity.this,codeResult.getMsg(),Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }





    public void toolBar(){
        boolean isImmersive = false;
        if (hasKitKat() && !hasLollipop()) {
            isImmersive = true;
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (hasLollipop()) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            isImmersive = true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        titleBar.setLeftImageResource(R.drawable.ic_left_black_24dp);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextSize(18);
        titleBar.setLeftTextColor(getResources().getColor(R.color.colorLightOrange));
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        titleBar.setTitleColor(Color.BLACK);

        //沉浸式
        titleBar.setImmersive(isImmersive);
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
