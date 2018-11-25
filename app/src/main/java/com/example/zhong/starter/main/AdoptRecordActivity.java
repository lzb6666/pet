package com.example.zhong.starter.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.zhong.starter.R;
import com.example.zhong.starter.account.LoginActivity;
import com.example.zhong.starter.adopt.adapter.AdoptListAdapter;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.main.adapter.RecordAdapter;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.vo.AdoptRecord;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdoptRecordActivity extends AppCompatActivity {

    private RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_record);

        RecyclerView recyclerView=findViewById(R.id.rylView_all_adopt);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new RecordAdapter(this);
        recyclerView.setAdapter(adapter);

        loadData();

    }

    private void loadData(){
        HttpUtil.sendGet("/adopt/records?userID=" + LogInfo.getUser().getUserID(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(()->{
                    Toast.makeText(AdoptRecordActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<AdoptRecord> recordList=JsonUtil.gson.fromJson(response.body().string(),new TypeToken<List<AdoptRecord>>(){}.getType());
                runOnUiThread(()->{
                    adapter.setRecords(recordList);
                });
            }
        });
    }
}
