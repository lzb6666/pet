package com.example.zhong.starter.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhong.starter.R;
import com.example.zhong.starter.adopt.PetDetailActivity;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        nameTxtView=findViewById(R.id.txtView_record_detail_name);
        ownerTxtView=findViewById(R.id.txtView_record_detail_owner);
        timeTxtView=findViewById(R.id.txtView_record_detail_time);
        desTxtView=findViewById(R.id.txtView_record_detail_des);
        petImgView=findViewById(R.id.imgView_record_detail_pet);

        Button okBtn=findViewById(R.id.btn_record_detail_ok);
        okBtn.setOnClickListener(v->{
            finish();
        });

        Intent intent=getIntent();
        recordID= intent.getStringExtra("recordID");
        loadData(recordID);
    }

    private void loadData(String recordID){
        HttpUtil.sendGet("/adopt/record?recordID=" + recordID, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                AdoptRecord record=JsonUtil.gson.fromJson(response.body().string(),AdoptRecord.class);
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
                petImgView.setOnClickListener(v->{
                    Intent intentToPetDetail = new Intent(RecordDetailActivity.this, PetDetailActivity.class);
                    intentToPetDetail.putExtra("petID",record.getPetID());
                    intentToPetDetail.putExtra("from","record");
                    RecordDetailActivity.this.startActivity(intentToPetDetail);
                });
            }
        });

    }
}
