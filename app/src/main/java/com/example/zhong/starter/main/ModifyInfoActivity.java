package com.example.zhong.starter.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zhong.starter.R;
import com.example.zhong.starter.account.LoginActivity;
import com.example.zhong.starter.account.RegisterActivity;
import com.example.zhong.starter.adopt.ReleaseTaskActivity;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.TitleBar;
import com.example.zhong.starter.util.result.CodeResult;
import com.example.zhong.starter.vo.User;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyInfoActivity extends AppCompatActivity {
    private int position;
    private String title;
    private String content;
    private EditText editText;
    private RadioGroup sexRg;
    private User user;
    private String sex;

    private TitleBar titleBar;
    private static final String TAG="ModifyInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        position = (Integer) bundle.get("position");

        title = (String) bundle.get("title");
        content = (String) bundle.get("content");


        switch (position){
            case 0:
                setContentView(R.layout.activity_modify_info_radio);
                sexRg = findViewById(R.id.rg_sex);
                final String[] result = new String[1];
                sexRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // 获取选中的RadioButton的id
                        int id = group.getCheckedRadioButtonId();
                        // 通过id实例化选中的这个RadioButton
                        RadioButton radioButton = (RadioButton) findViewById(id);
                        // 获取这个RadioButton的text内容
                        result[0] = radioButton.getText().toString();
                        sex = result[0];

                        Toast.makeText(ModifyInfoActivity.this, "你的性别为：" + sex, Toast.LENGTH_SHORT).show();

                    }
                });
                break;
            case 1:
                setContentView(R.layout.activity_modify_info);
                editText = findViewById(R.id.edit_info);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setText(content);
                break;
            case 2:

            case 3:
                setContentView(R.layout.activity_modify_info);
                editText = findViewById(R.id.edit_info);
                editText.setText(content);
                break;
            case 4:

            case 5:
                setContentView(R.layout.activity_modify_info_multi);
                editText = findViewById(R.id.edit_info);
                editText.setText(content);
                break;
        }

        toolbar();






    }

    private void toolbar(){
        titleBar = (TitleBar) findViewById(R.id.toolbar);

        titleBar.setLeftImageResource(R.drawable.ic_left_black_24dp);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(Color.BLACK);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setTitle(title);

        //右侧
        titleBar.addAction(new TitleBar.TextAction("完成") {
            @Override
            public void performAction(View view) {
                //发送请求修改信息
                if(position!=0)
                    editText.getText();

                switch (position){
                    case 0:
                        LogInfo.getUser(ModifyInfoActivity.this).setSex(sex);
                        break;
                    case 1:
                        LogInfo.getUser(ModifyInfoActivity.this).setAge(editText.getText().toString());
                        break;
                    case 2:
                        LogInfo.getUser(ModifyInfoActivity.this).setCareer(editText.getText().toString());
                        break;
                    case 3:
                        LogInfo.getUser(ModifyInfoActivity.this).setCity(editText.getText().toString());
                        break;
                    case 4:
                        LogInfo.getUser(ModifyInfoActivity.this).setPetExperience(editText.getText().toString());
                        break;
                    case 5:
                        LogInfo.getUser(ModifyInfoActivity.this).setPreference(editText.getText().toString());
                        break;
                }
                user = LogInfo.getUser(ModifyInfoActivity.this);
                LogInfo.setUser(ModifyInfoActivity.this,user);
                updateInfo();

            }
        });
        titleBar.setTitleColor(Color.BLACK);

    }


    private void updateInfo(){

        RequestBody requestBody=new FormBody.Builder()
                .add("userID",user.getUserID())
                .add("username",user.getUsername())
                .add("sex",user.getSex())
                .add("age",user.getAge())
                .add("career",user.getCareer())
                .add("city",user.getCity())
                .add("petExperience",user.getPetExperience())
                .add("preference",user.getPreference())
                .build();
        HttpUtil.sendPost("/account/update", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ModifyInfoActivity.this.runOnUiThread(()->{
                    Toast.makeText(ModifyInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CodeResult codeResult=JsonUtil.gson.fromJson(response.body().string(),CodeResult.class);
                Log.d(TAG, "onResponse: "+codeResult.getMsg());
                if (codeResult.getRstCode()==200){
                    ModifyInfoActivity.this.runOnUiThread(()->{
                        finish();
                    });
                }

                if (codeResult.getRstCode()==400){
                    Log.d(TAG, "onResponse: "+codeResult.getMsg());
                    ModifyInfoActivity.this.runOnUiThread(()->{
                        Toast.makeText(ModifyInfoActivity.this,codeResult.getMsg(),Toast.LENGTH_SHORT).show();
                    });
                }


            }
        });
    }
}
