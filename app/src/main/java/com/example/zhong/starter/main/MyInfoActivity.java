package com.example.zhong.starter.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhong.starter.R;
import com.example.zhong.starter.adopt.ReleaseTaskActivity;
import com.example.zhong.starter.base.MyListView;
import com.example.zhong.starter.main.adapter.MyInfoAdapter;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.TitleBar;
import com.example.zhong.starter.vo.Pet;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyInfoActivity extends AppCompatActivity {

    private TextView account;
    private RecyclerView rv;
    private MyInfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        toolBar();
        initView();

        setRecyclerView();

        loadListData();
    }

    public void toolBar(){

        final TitleBar titleBar = (TitleBar) findViewById(R.id.toolbar);

        //titleBar.setDividerColor(Color.GRAY);
        //左侧
        titleBar.setLeftImageResource(R.drawable.ic_left_black_24dp);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(getResources().getColor(R.color.colorWhite));
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //右侧
        //titleBar.addAction(new TitleBar.TextAction("发布领养") {
        //    @Override
        //    public void performAction(View view) {
        //        //跳转到发布页面
        //        Intent intent = new Intent(MyInfoActivity.this, ReleaseTaskActivity.class);
        //        startActivity(intent);
        //
        //    }
        //});

    }

    private void initView(){
        account = findViewById(R.id.textView_accout);
        rv = findViewById(R.id.rv_info);
    }

    private void setRecyclerView(){

        rv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyInfoAdapter(MyInfoActivity.this);
        mAdapter.setItemClickListener(new MyInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MyInfoActivity.this, ""+position, Toast.LENGTH_SHORT).show();
            }
        });


        rv.setAdapter(mAdapter);

    }

    private void loadListData(){
        HttpUtil.sendGet("/pet/pets?start=0&end=20", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyInfoActivity.this.runOnUiThread(()->{
                    Toast.makeText(MyInfoActivity.this.getApplicationContext(),"加载失败",Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Pet> pets= JsonUtil.gson.fromJson(response.body().string(),new TypeToken<List<Pet>>(){}.getType());
                ArrayList<String> titles = new ArrayList<String>(Arrays.asList("性别", "年龄","职业"));
                MyInfoActivity.this.runOnUiThread(()->{
                    mAdapter.setTitles(titles);
                    mAdapter.setContents(titles);
                });
            }
        });
    }
}
