package com.example.zhong.starter.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.zhong.starter.R;
import com.example.zhong.starter.adopt.adapter.FosterListAdapter;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.TitleBar;
import com.example.zhong.starter.vo.Nurse;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NurseRecord_SendActivity extends AppCompatActivity {

    private FosterListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_record__send);

        toolbar();

        RecyclerView recyclerView=findViewById(R.id.rylView_foster_petList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(NurseRecord_SendActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FosterListAdapter(NurseRecord_SendActivity.this);
        recyclerView.setAdapter(adapter);

        loadListData();
    }

    private void toolbar(){
        TitleBar titleBar = (TitleBar) findViewById(R.id.toolbar);
        //左侧
        titleBar.setLeftImageResource(R.drawable.ic_left_black_24dp);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(getResources().getColor(R.color.colorBlack));
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBar.setTitleColor(Color.BLACK);
    }
    private void loadListData(){
        HttpUtil.sendGet("/nurse/sendRecords?userID="+ LogInfo.getUser(NurseRecord_SendActivity.this).getUserID(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                NurseRecord_SendActivity.this.runOnUiThread(()->{
                    Toast.makeText(NurseRecord_SendActivity.this.getApplicationContext(),"加载失败",Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Nurse> pets= JsonUtil.gson.fromJson(response.body().string(),new TypeToken<List<Nurse>>(){}.getType());
                System.out.println(pets);
                NurseRecord_SendActivity.this.runOnUiThread(()->{
                    adapter.setNurseList(pets);
                    adapter.setType("foster");
                });
            }
        });
    }
}
