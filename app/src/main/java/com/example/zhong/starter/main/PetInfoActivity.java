package com.example.zhong.starter.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.zhong.starter.R;
import com.example.zhong.starter.adopt.adapter.AdoptListAdapter;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.main.adapter.PetInfoAdapter;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.TitleBar;
import com.example.zhong.starter.vo.Pet;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PetInfoActivity extends AppCompatActivity {

    private TitleBar titleBar;
    private PetInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info);

        toolbar();

        RecyclerView recyclerView = findViewById(R.id.rylView_foster_petList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(PetInfoActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new PetInfoAdapter(PetInfoActivity.this);
        recyclerView.setAdapter(adapter);

        loadListData();

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
        titleBar.setTitle("宠物信息");

        //右侧
        titleBar.addAction(new TitleBar.TextAction("添加宠物") {
            @Override
            public void performAction(View view) {
                Intent intentToAddPet = new Intent(PetInfoActivity.this,AddPetActivity.class);
                startActivity(intentToAddPet);

            }
        });
        titleBar.setTitleColor(Color.BLACK);

    }

    private void loadListData(){
        HttpUtil.sendGet("/pet/my?userID="+LogInfo.getUser(PetInfoActivity.this).getUserID(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PetInfoActivity.this.runOnUiThread(()->{
                    Toast.makeText(PetInfoActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Pet> pets= JsonUtil.gson.fromJson(response.body().string(),new TypeToken<List<Pet>>(){}.getType());
                PetInfoActivity.this.runOnUiThread(()->{
                    adapter.setPetList(pets);
                    adapter.setType("foster");
                });
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        loadListData();

    }
}
