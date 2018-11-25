package com.example.zhong.starter.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhong.starter.R;
import com.example.zhong.starter.adopt.PetDetailActivity;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.TitleBar;
import com.example.zhong.starter.vo.AdoptRecord;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecordDetailActivity extends AppCompatActivity {

    private TextView nameTxtView;
    private TextView ownerTxtView;
    private TextView timeTxtView;
    private TextView desTxtView;
    private ImageView petImgView;
    private String recordID;
    private AdoptRecord record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        toolbar();

        Intent intent=getIntent();
        recordID= intent.getStringExtra("recordID");
        loadData(recordID);

        initView();


        //Button okBtn=findViewById(R.id.btn_record_detail_ok);
        //okBtn.setOnClickListener(v->{
        //    finish();
        //});


        petImgView.setOnClickListener(v->{
            Intent intentToPetDetail = new Intent(RecordDetailActivity.this, PetDetailActivity.class);
            intentToPetDetail.putExtra("petID",record.getPetID());
            intentToPetDetail.putExtra("from","record");
            RecordDetailActivity.this.startActivity(intentToPetDetail);
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

        titleBar.setTitle("记录详情");
        titleBar.setTitleColor(Color.BLACK);

    }

    private void initView(){
        nameTxtView=findViewById(R.id.txtView_record_detail_name);
        ownerTxtView=findViewById(R.id.txtView_record_detail_owner);
        timeTxtView=findViewById(R.id.txtView_record_detail_time);
        desTxtView=findViewById(R.id.txtView_record_detail_des);
        petImgView=findViewById(R.id.imgView_record_detail_pet);
    }

    private void loadData(String recordID){
        HttpUtil.sendGet("/adopt/record?recordID=" + recordID, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                record=JsonUtil.gson.fromJson(response.body().string(),AdoptRecord.class);
                runOnUiThread(()->{
                    nameTxtView.setText(record.getPetName());
                    ownerTxtView.setText(record.getOwner());
                    timeTxtView.setText(record.getAdoptTime());
                    desTxtView.setText(record.getPetDetail());
                    Glide.with(RecordDetailActivity.this)
                            .load(record.getImgURL())
                            .placeholder(R.drawable.ic_pets_black_24dp)
                            .error(R.drawable.ic_pets_black_24dp)
                            .into(petImgView);
                });

            }
        });

    }
}
