package com.example.zhong.starter.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zhong.starter.R;
import com.example.zhong.starter.adopt.adapter.FosterListAdapter;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.vo.Nurse;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuditedTabFragment extends Fragment {


    private FosterListAdapter adapter;

    public AuditedTabFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        AuditedTabFragment fragment = new AuditedTabFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audited_tab, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rylView_foster_petList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FosterListAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        loadListData();
        return view;
    }

    private void loadListData(){
        HttpUtil.sendGet("/nurse/sendRecords?userID="+ LogInfo.getUser(getActivity()).getUserID(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(()->{
                    Toast.makeText(getActivity().getApplicationContext(),"加载失败",Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<Nurse> pets= JsonUtil.gson.fromJson(response.body().string(),new TypeToken<List<Nurse>>(){}.getType());
                System.out.println(pets);
                getActivity().runOnUiThread(()->{
                    adapter.setNurseList(pets);
                    adapter.setType("foster_send_audited");
                });
            }
        });
    }

}
