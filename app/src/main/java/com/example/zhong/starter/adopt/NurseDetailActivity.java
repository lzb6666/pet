package com.example.zhong.starter.adopt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhong.starter.R;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.TitleBar;
import com.example.zhong.starter.util.result.CodeResult;
import com.example.zhong.starter.vo.Nurse;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NurseDetailActivity extends AppCompatActivity {

    private static final String TAG = "NurseDetailActivity";
    private TextView nameTxtView;
    private TextView detailTxtView;
    private TextView varietyTxtView;
    private TextView ageTxtView;
    private TextView sexTxtView;
    private TextView healthTxtView;
    private TextView noteTxtView;
    //private TextView phoneNumTxt;
    private ImageView imageView;
    private Button adoptBtn;
    private String nurseID;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();

        nurseID = intent.getStringExtra("nurseID");
        type = intent.getStringExtra("from");

        switch (type){
            case "record":
            case"foster_receive":
                setContentView(R.layout.activity_nurse_detail);
                adoptBtn=findViewById(R.id.btn_pet_adopt);
                adoptBtn.setVisibility(View.INVISIBLE);
                break;
            case "foster_send_audited":
                setContentView(R.layout.activity_nurse_detail_send);
                TextView tv_status = findViewById(R.id.textView_status);
                tv_status.setText("已审核");
                tv_status.setTextColor(Color.parseColor("#75BD47"));
                break;
            case "foster_send_unaudited":
                setContentView(R.layout.activity_nurse_detail_send);
                break;
            case "foster_send_under":
                setContentView(R.layout.activity_nurse_detail_send_under);
                Button adoptBtnAgree=findViewById(R.id.btn_pet_send_agree);
                Button adoptBtnRefuse=findViewById(R.id.btn_pet_send_refuse);

                adoptBtnAgree.setOnClickListener(v->{
                    agree(LogInfo.getUser(NurseDetailActivity.this).getUserID(),nurseID);
                });

                adoptBtnRefuse.setOnClickListener(v->{
                    refuse(LogInfo.getUser(NurseDetailActivity.this).getUserID(),nurseID);
                });

                break;
            default:
                setContentView(R.layout.activity_nurse_detail);
                adoptBtn=findViewById(R.id.btn_pet_adopt);
                adoptBtn.setOnClickListener(v->{
                    adopt(LogInfo.getUser(NurseDetailActivity.this).getUserID(),nurseID);
                });
                break;

        }

        toolbar();


        initView();




        getPet(nurseID);
    }

    private void initView(){
        nameTxtView=findViewById(R.id.textView_name_detail_pet);
        detailTxtView=findViewById(R.id.textView_description_detail_pet);
        imageView=findViewById(R.id.imageView_detail_pet);
        varietyTxtView=findViewById(R.id.TxtView_pet_variety);
        ageTxtView=findViewById(R.id.TxtView_pet_age);
        sexTxtView=findViewById(R.id.TxtView_pet_sex);
        healthTxtView=findViewById(R.id.TxtView_pet_health);
        noteTxtView =findViewById(R.id.TxtView_pet_other);
        //phoneNumTxt=findViewById(R.id.TxtView_pet_phoneNum);

    }

    private void getPet(String petID){
        Log.d(TAG, "getNursePet: "+petID);
        RequestBody requestBody=new FormBody.Builder()
                .add("nurseID",petID)
                .build();
        HttpUtil.sendPost("/nurse",requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                NurseDetailActivity.this.runOnUiThread(()->{
                    Toast.makeText(NurseDetailActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body()==null){
                    return;
                }
                Nurse pet= JsonUtil.gson.fromJson(response.body().string(),Nurse.class);
                Log.d(TAG, "onResponse: "+pet.getName());
                runOnUiThread(()->{
                    nameTxtView.setText(pet.getName());
                    detailTxtView.setText(pet.getOther());
                    varietyTxtView.setText(pet.getVariety());
                    ageTxtView.setText(pet.getAge());
                    sexTxtView.setText(pet.getSex());
                    healthTxtView.setText(pet.getHealth());
                    noteTxtView.setText(pet.getNote());
                    //phoneNumTxt.setText(pet.getPhoneNum());
                    Glide.with(NurseDetailActivity.this)
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
                .add("nurseID",petID)
                .add("userID",userID)
                .build();
        HttpUtil.sendPost("/nurse/apply",requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                NurseDetailActivity.this.runOnUiThread(()->{
                    Toast.makeText(NurseDetailActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body()==null){
                    return;
                }
                CodeResult codeResult=JsonUtil.gson.fromJson(response.body().string(),CodeResult.class);
                Log.d(TAG, "onResponse: "+codeResult.getMsg());
                NurseDetailActivity.this.runOnUiThread(()->{
                    Toast.makeText(NurseDetailActivity.this,codeResult.getMsg(),Toast.LENGTH_SHORT).show();
                    if (codeResult.getRstCode()==200){
                        adoptBtn.setEnabled(false);
                    }
                });
            }
        });
    }

    private void refuse(String userID,String petID){
        RequestBody requestBody=new FormBody.Builder()
                .add("nurseID",petID)
                .add("result","no")
                .build();
        HttpUtil.sendPost("/nurse/submit",requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                NurseDetailActivity.this.runOnUiThread(()->{
                    Toast.makeText(NurseDetailActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body()==null){
                    return;
                }
                CodeResult codeResult=JsonUtil.gson.fromJson(response.body().string(),CodeResult.class);
                Log.d(TAG, "onResponse: "+codeResult.getMsg());
                NurseDetailActivity.this.runOnUiThread(()->{
                    Toast.makeText(NurseDetailActivity.this,codeResult.getMsg(),Toast.LENGTH_SHORT).show();
                    if (codeResult.getRstCode()==200){
                        //Toast.makeText(NurseDetailActivity.this,codeResult.getMsg(),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    private void agree (String userID,String petID){
        RequestBody requestBody=new FormBody.Builder()
                .add("nurseID",petID)
                .add("result","ok")
                .build();
        HttpUtil.sendPost("/nurse/submit",requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                NurseDetailActivity.this.runOnUiThread(()->{
                    Toast.makeText(NurseDetailActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body()==null){
                    return;
                }
                CodeResult codeResult=JsonUtil.gson.fromJson(response.body().string(),CodeResult.class);
                Log.d(TAG, "onResponse: "+codeResult.getMsg());
                NurseDetailActivity.this.runOnUiThread(()->{
                    Toast.makeText(NurseDetailActivity.this,codeResult.getMsg(),Toast.LENGTH_SHORT).show();
                    if (codeResult.getRstCode()==200){
                        finish();
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

        titleBar.setTitle("详情");
        titleBar.setTitleColor(Color.BLACK);

    }
}
