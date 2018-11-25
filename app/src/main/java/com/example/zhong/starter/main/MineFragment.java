package com.example.zhong.starter.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhong.starter.R;
import com.example.zhong.starter.account.LoginActivity;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.main.adapter.MineAdapter;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.ImgUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.result.CodeResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class MineFragment extends Fragment {

    private Bitmap bitmap;
    private ImageView headImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, null);
        //accountInfoLayout = (RelativeLayout)view.findViewById(R.id.linearlayout_account_info_account);
        TextView accountTextView = view.findViewById(R.id.textview_account);
        accountTextView.setText(LogInfo.getUser(getContext()).getUsername());
        TextView citiAccountTextView = view.findViewById(R.id.textview_account_position);
        headImg = view.findViewById(R.id.headImg_account);
        Glide.with(view.getContext())
                .load(LogInfo.getUser(getContext()).getHeadImgURL())
                .placeholder(R.drawable.portrait_1)
                .error(R.drawable.portrait_1)
                .into(headImg);

        Button logoutBtn=view.findViewById(R.id.btn_logout_account);
        logoutBtn.setOnClickListener(v->{
            LogInfo.setUser(getContext(),null);
            LogInfo.getInstance(getContext()).logout();
            Intent intent=new Intent(view.getContext(),LoginActivity.class);
            startActivity(intent);
        });

        headImg.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 1);
        });
        ArrayList<String> menuItem = new ArrayList<String>(Arrays.asList("领养记录", "我的订单", "设置"));
        ArrayList<Integer> menuIcon = new ArrayList<Integer>(Arrays.asList(R.drawable.ic_reorder_black_24dp, R.drawable.ic_reorder_black_24dp, R.drawable.ic_reorder_black_24dp));
        ListView mineListView = view.findViewById(R.id.listView_menu_account);
        MineAdapter mineAdapter = new MineAdapter(menuItem, menuIcon, getContext());
        mineListView.setAdapter(mineAdapter);

        mineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intentToRecord = new Intent(getContext(), AdoptRecordActivity.class);
                        startActivity(intentToRecord);
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requsetCode, int resultCode, Intent data) {
        super.onActivityResult(requsetCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requsetCode) {
                case 1:
                    startSmallPhotoZoom(data.getData());
                    break;
                case 2:
                    setPicToView(data);
                    break;
            }
        }
    }

    public void startSmallPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 170); // 输出图片大小
        intent.putExtra("outputY", 170);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    private void setPicToView(Intent data) {
        Bundle extras = data.getExtras();
        bitmap = extras.getParcelable("data");
        upload();
        headImg.setImageBitmap(bitmap);
    }

    private void upload() {
        File file = ImgUtil.compressImage(bitmap);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("headImg", "head.png", RequestBody.create(MediaType.parse("image/jpg"), file))
                .addFormDataPart("userID",LogInfo.getUser(getContext()).getUserID())
                .build();
        HttpUtil.sendPost("/account/uploadHeadImg", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity().getApplicationContext(), "头像上传失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CodeResult codeResult = JsonUtil.gson.fromJson(response.body().string(), CodeResult.class);
                Log.d(TAG, "onResponse: " + codeResult.getMsg());
                if (codeResult.getRstCode() == 200) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity().getApplicationContext(), "头像上传成功", Toast.LENGTH_SHORT).show();
                    });
                }
                if (codeResult.getRstCode() == 400) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity().getApplicationContext(), codeResult.getMsg(), Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }
}
