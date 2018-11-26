package com.example.zhong.starter.adopt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zhong.starter.R;
import com.example.zhong.starter.adopt.adapter.AdoptListAdapter;
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

public class FosterFragment extends Fragment {
    private View view;
    private AdoptListAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_foster, null);
        toolbar();

        RecyclerView recyclerView=view.findViewById(R.id.rylView_foster_petList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new AdoptListAdapter(this.getContext());
        recyclerView.setAdapter(adapter);

        loadListData();
        return view;
    }

    private void toolbar(){
        TitleBar titleBar = (TitleBar) view.findViewById(R.id.toolbar);

        //右侧
        titleBar.addAction(new TitleBar.TextAction("发布寄养") {
            @Override
            public void performAction(View view) {
                //跳转到发布页面
                Intent intent = new Intent(getActivity(), ReleaseTaskActivity.class);
                startActivity(intent);

            }
        });

        titleBar.setTitleColor(Color.BLACK);
    }
    private void loadListData(){
        HttpUtil.sendGet("/pet/pets", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(()->{
                    Toast.makeText(getActivity().getApplicationContext(),"加载失败",Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Pet> pets=JsonUtil.gson.fromJson(response.body().string(),new TypeToken<List<Pet>>(){}.getType());
                getActivity().runOnUiThread(()->{
                    adapter.setPetList(pets);
                    adapter.setType("foster");
                });
            }
        });
    }

}
