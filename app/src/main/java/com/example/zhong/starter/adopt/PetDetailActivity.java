package com.example.zhong.starter.adopt;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhong.starter.R;
import com.example.zhong.starter.account.LoginActivity;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.TitleBar;
import com.example.zhong.starter.util.result.CodeResult;
import com.example.zhong.starter.vo.Pet;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PetDetailActivity extends AppCompatActivity {

    private static final String TAG = "PetDetailActivity";
    private TextView nameTxtView;
    private TextView detailTxtView;
    private TextView varietyTxtView;
    private TextView ageTxtView;
    private TextView sexTxtView;
    private TextView healthTxtView;
    private TextView otherTxtView;
    private TextView phoneNumTxt;
    private ImageView imageView;
    private Button adoptBtn;
    private String petID;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);
        toolbar();

        nameTxtView=findViewById(R.id.textView_name_detail_pet);
        detailTxtView=findViewById(R.id.textView_description_detail_pet);
        imageView=findViewById(R.id.imageView_detail_pet);
        varietyTxtView=findViewById(R.id.TxtView_pet_variety);
        ageTxtView=findViewById(R.id.TxtView_pet_age);
        sexTxtView=findViewById(R.id.TxtView_pet_sex);
        healthTxtView=findViewById(R.id.TxtView_pet_health);
        otherTxtView=findViewById(R.id.TxtView_pet_other);
        phoneNumTxt=findViewById(R.id.TxtView_pet_phoneNum);
        adoptBtn=findViewById(R.id.btn_pet_adopt);
        Intent intent=getIntent();
        petID=intent.getStringExtra("petID");
        if (intent.getStringExtra("from").equals("record")){
            adoptBtn.setVisibility(View.INVISIBLE);
            url="/pet/myPet";

        }else{
            adoptBtn.setOnClickListener(v->{
                adopt(LogInfo.getUser(PetDetailActivity.this).getUserID(),petID);
            });
            url="/pet";
        }
        getPet(petID);
    }

    private void getPet(String petID){
        Log.d(TAG, "getPet: "+petID);
        RequestBody requestBody=new FormBody.Builder()
                .add("petID",petID)
                .build();
        HttpUtil.sendPost(url,requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PetDetailActivity.this.runOnUiThread(()->{
                    Toast.makeText(PetDetailActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body()==null){
                    return;
                }
                Pet pet=JsonUtil.gson.fromJson(response.body().string(),Pet.class);
                Log.d(TAG, "onResponse: "+pet.getName());
                runOnUiThread(()->{
                    nameTxtView.setText(pet.getName());
                    detailTxtView.setText(pet.getDetail());
                    varietyTxtView.setText(pet.getVariety());
                    ageTxtView.setText(pet.getAge());
                    sexTxtView.setText(pet.getSex());
                    healthTxtView.setText(pet.getHealthStatus());
                    otherTxtView.setText(pet.getOther());
                    phoneNumTxt.setText(pet.getPhoneNum());
                    Glide.with(PetDetailActivity.this)
                            .load(pet.getImgURL())
                            .placeholder(R.drawable.ic_pets_black_24dp)
                            .error(R.drawable.ic_pets_black_24dp)
                            .into(imageView);
                });
            }
        });
    }

    private void adopt(String userID,String petID){
        RequestBody requestBody=new FormBody.Builder()
                .add("petID",petID)
                .add("userID",userID)
                .build();
        HttpUtil.sendPost("/adopt",requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PetDetailActivity.this.runOnUiThread(()->{
                    Toast.makeText(PetDetailActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body()==null){
                    return;
                }
                CodeResult codeResult=JsonUtil.gson.fromJson(response.body().string(),CodeResult.class);
                Log.d(TAG, "onResponse: "+codeResult.getMsg());
                PetDetailActivity.this.runOnUiThread(()->{
                    Toast.makeText(PetDetailActivity.this,codeResult.getMsg(),Toast.LENGTH_SHORT).show();
                    if (codeResult.getRstCode()==200){
                        adoptBtn.setEnabled(false);
                    }
                });
            }
        });
    }

    private void toolbar(){
        TitleBar titleBar = (TitleBar) findViewById(R.id.toolbar);

        titleBar.setLeftImageResource(R.drawable.ic_left_black_24dp);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(Color.BLACK);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBar.setTitle("宠物详情");
        titleBar.setTitleColor(Color.BLACK);

    }
}
