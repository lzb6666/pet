package com.example.zhong.starter.adopt;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.zhong.starter.R;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.main.MainActivity;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.ImgUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.result.CodeResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class FosterFragment extends Fragment {
    private static final String TAG ="FosterFragment" ;
    private View view;
    private EditText nameEditTxt;
    private EditText detailEditTxt;
    private RadioGroup sexRg;
    private EditText ageTxt;
    private EditText healthTxt;
    private EditText otherTxt;
    private EditText varietyTxt;
    private ImageView petImgView;
    private Bitmap bitmap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_foster, null);

        nameEditTxt=view.findViewById(R.id.edtTxt_pet_name);
        detailEditTxt=view.findViewById(R.id.edtTxt_pet_detail);
        petImgView=view.findViewById(R.id.imgView_pet_img);
        sexRg=view.findViewById(R.id.rg_sex);
        ageTxt=view.findViewById(R.id.edtTxt_pet_age);
        healthTxt=view.findViewById(R.id.edtTxt_pet_health);
        otherTxt=view.findViewById(R.id.edtTxt_pet_other);
        varietyTxt=view.findViewById(R.id.edtTxt_pet_variety);
        petImgView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 1);
        });

        Button submitBtn=view.findViewById(R.id.btn_pet_submit);
        submitBtn.setOnClickListener(v->{
            String name=nameEditTxt.getText().toString();
            String detail=detailEditTxt.getText().toString();
            if (name.length()==0){
                Toast.makeText(this.getContext(),"请填写宠物的名字",Toast.LENGTH_SHORT).show();
            }
            if (detail.length()==0){
                Toast.makeText(this.getContext(),"请填写宠物的描述",Toast.LENGTH_SHORT).show();
            }
            if (bitmap==null){
                Toast.makeText(this.getContext(),"请选择宠物图片",Toast.LENGTH_SHORT).show();
            }
            RadioButton radioButton=view.findViewById(sexRg.getCheckedRadioButtonId());
            String sex=radioButton.getText().toString();
            String age=ageTxt.getText().toString();
            String health=healthTxt.getText().toString();
            String other=otherTxt.getText().toString();
            String variety=varietyTxt.getText().toString();
            this.upload(name,detail,sex,age,health,other,variety);
        });
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }


    @Override
    public void onActivityResult(int requsetCode,int resultCode,Intent data){
        super.onActivityResult(requsetCode,resultCode,data);
        if (resultCode==RESULT_OK){
            switch (requsetCode){
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
        petImgView.setImageBitmap(bitmap);
    }

    private void upload(String name,String detail,String sex,String age,String health,String other,String variety){
        File file=ImgUtil.compressImage(bitmap);
        RequestBody requestBody=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("petImg","pet.png",RequestBody.create(MediaType.parse("image/jpg"), file))
                .addFormDataPart("name",name)
                .addFormDataPart("detail",detail)
                .addFormDataPart("variety",variety)
                .addFormDataPart("sex",sex)
                .addFormDataPart("age",age)
                .addFormDataPart("health",health)
                .addFormDataPart("other",other)
                .addFormDataPart("userID",LogInfo.getUser().getUserID())
                .build();
        HttpUtil.sendPost("/pet/upload", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(()->{
                    Toast.makeText(getActivity().getApplicationContext(),"宠物上传失败",Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CodeResult codeResult=JsonUtil.gson.fromJson(response.body().string(),CodeResult.class);
                Log.d(TAG, "onResponse: "+codeResult.getMsg());
                if (codeResult.getRstCode()==200){
                    getActivity().runOnUiThread(()->{
                        Toast.makeText(getActivity().getApplicationContext(),"宠物上传成功",Toast.LENGTH_SHORT).show();
                        //TODO:跳转至list
                        Intent intent=new Intent("com.pet.broadcast.TO_ADOPT");
                        LocalBroadcastManager.getInstance(FosterFragment.this.getContext()).sendBroadcast(intent);
                    });
                }
                if (codeResult.getRstCode()==400){
                    getActivity().runOnUiThread(()->{
                        Toast.makeText(getActivity().getApplicationContext(),codeResult.getMsg(),Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

}
