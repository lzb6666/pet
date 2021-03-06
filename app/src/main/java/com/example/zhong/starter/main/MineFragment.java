package com.example.zhong.starter.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhong.starter.R;
import com.example.zhong.starter.account.LoginActivity;
import com.example.zhong.starter.base.MyListView;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.main.adapter.MineAdapter;
import com.example.zhong.starter.util.CropUtil;
import com.example.zhong.starter.util.GallaryUtil;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.result.CodeResult;
import com.example.zhong.starter.util.PermissionUtil;

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


    private ImageView headImg;
    private Uri uriTempFile;

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
            if (!PermissionUtil.getPermission(getActivity())){
                return;
            }
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 1);
        });
        ArrayList<String> menuItem = new ArrayList<String>(Arrays.asList("个人信息", "宠物信息", "领养记录", "寄养记录","设置"));
        ArrayList<Integer> menuIcon = new ArrayList<Integer>(Arrays.asList(R.drawable.ic_reorder_black_24dp, R.drawable.ic_reorder_black_24dp, R.drawable.ic_reorder_black_24dp, R.drawable.ic_reorder_black_24dp, R.drawable.ic_reorder_black_24dp));
        MyListView mineListView = view.findViewById(R.id.listView_menu_account);
        MineAdapter mineAdapter = new MineAdapter(menuItem, menuIcon, getContext());
        setListViewHeightBasedOnChildren(mineListView);
        mineListView.setAdapter(mineAdapter);

        mineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intentToInfo = new Intent(getContext(), MyInfoActivity.class);
                        startActivity(intentToInfo);
                        break;
                    case 1:
                        Intent intentToPetInfo = new Intent(getContext(), PetInfoActivity.class);
                        startActivity(intentToPetInfo);
                        break;
                    case 2:
                        Intent intentToAdoptRecord = new Intent(getContext(), AdoptRecordActivity.class);
                        startActivity(intentToAdoptRecord);
                        break;
                    case 3:
                        Intent intentToFosterRecord = new Intent(getContext(), NurseRecordActivity.class);
                        startActivity(intentToFosterRecord);
                        break;

                    case 4:
                        Intent intentToSetting = new Intent(getContext(), SettingActivity.class);
                        startActivity(intentToSetting);
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
                    uriTempFile=CropUtil.startSmallPhotoZoom(data.getData(),getActivity());
                    break;
                case 2:
                    //String path=GallaryUtil.getRealPathFromUri(this.getActivity(), data.getData());
                    headImg.setImageURI(uriTempFile);
                    upload();
                    break;
            }
        }
    }

   /* public void startSmallPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        String path = Environment.getExternalStorageDirectory() + "/take_photo";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        uriTempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath()+"/take_photo" + "/" + System.currentTimeMillis() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);

        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1); // 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 170); // 输出图片大小
        intent.putExtra("outputY", 170);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }*/

/*    private void setPicToView() {
        *//*Bundle extras = data.getExtras();
        bitmap = extras.getParcelable("data");*//*

        //headImg.setImageBitmap(bitmap);
    }*/

    private void upload() {
        String path=GallaryUtil.getRealPathFromUri(this.getActivity(), uriTempFile);
        File file=new File(path);
        //File file = ImgUtil.compressImage(bitmap);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("headImg", "head.png", RequestBody.create(MediaType.parse("image/jpg"), file))
                .addFormDataPart("userID",LogInfo.getUser(getContext()).getUserID())
                .build();
        HttpUtil.sendPost("/account/uploadHeadImg", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (getActivity()==null)return;
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity().getApplicationContext(), "头像上传失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (getActivity()==null)return;
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

    public void setListViewHeightBasedOnChildren(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0); // 计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        listView.setLayoutParams(params);

    }
}
