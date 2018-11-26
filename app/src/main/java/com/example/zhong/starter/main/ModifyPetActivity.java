package com.example.zhong.starter.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhong.starter.R;
import com.example.zhong.starter.adopt.ReleaseTaskActivity;
import com.example.zhong.starter.data.LogInfo;
import com.example.zhong.starter.util.HttpUtil;
import com.example.zhong.starter.util.ImgUtil;
import com.example.zhong.starter.util.JsonUtil;
import com.example.zhong.starter.util.TitleBar;
import com.example.zhong.starter.util.result.CodeResult;
import com.example.zhong.starter.vo.Pet;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyPetActivity extends AppCompatActivity {

    private static final String TAG ="ModifyPetActivity" ;
    private EditText nameEditTxt;
    private EditText detailEditTxt;
    private RadioGroup sexRg;
    private EditText ageTxt;
    private EditText healthTxt;
    private EditText otherTxt;
    private EditText varietyTxt;
    private ImageView petImgView;
    private Bitmap bitmap;
    private Button submitBtn;

    private String petID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_task);

        Bundle bundle = getIntent().getExtras();
        petID = (String) bundle.get("petID");


        toolbar();

        initView();
        petImgView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 1);
        });

        //RadioButton radioButton = findViewById(sexRg.getCheckedRadioButtonId());
        final String[] result = new String[1];
        sexRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 获取选中的RadioButton的id
                int id = group.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton radioButton = (RadioButton) findViewById(id);
                // 获取这个RadioButton的text内容
                result[0] = radioButton.getText().toString();

                Toast.makeText(ModifyPetActivity.this, "你的性别为：" + result[0], Toast.LENGTH_SHORT).show();

            }
        });


        submitBtn.setOnClickListener(v->{
            String name=nameEditTxt.getText().toString();
            String detail=detailEditTxt.getText().toString();
            if (name.length()==0){
                Toast.makeText(ModifyPetActivity.this,"请填写宠物的名字",Toast.LENGTH_SHORT).show();
                return;
            }

            String sex = result[0];
            String age=ageTxt.getText().toString();
            String health=healthTxt.getText().toString();
            String other=otherTxt.getText().toString();
            String variety=varietyTxt.getText().toString();
            this.upload(name, detail, sex, age, health, other, variety);
        });

        getPet(petID);

    }

    private void initView(){
        nameEditTxt = findViewById(R.id.edtTxt_pet_name);
        detailEditTxt = findViewById(R.id.edtTxt_pet_detail);
        petImgView = findViewById(R.id.imgView_pet_img);
        sexRg = findViewById(R.id.rg_sex);
        ageTxt = findViewById(R.id.edtTxt_pet_age);
        healthTxt = findViewById(R.id.edtTxt_pet_health);
        otherTxt = findViewById(R.id.edtTxt_pet_other);
        varietyTxt = findViewById(R.id.edtTxt_pet_variety);
        submitBtn = findViewById(R.id.btn_pet_submit);
    }

    private void toolbar(){
        TitleBar titleBar = (TitleBar) findViewById(R.id.toolbar);

        titleBar.setLeftImageResource(R.drawable.ic_left_black_24dp);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(Color.BLACK);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBar.setTitle("修改宠物信息");
        titleBar.setTitleColor(Color.BLACK);

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
        if (bitmap==null){
            petImgView.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(petImgView.getDrawingCache());
            petImgView.setDrawingCacheEnabled(false);



        }
        File file= ImgUtil.compressImage(bitmap);
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
                .addFormDataPart("petID", petID)
                .build();
        HttpUtil.sendPost("/pet/updatePet", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ModifyPetActivity.this.runOnUiThread(()->{
                    Toast.makeText(ModifyPetActivity.this.getApplicationContext(),"宠物上传失败",Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                CodeResult codeResult= JsonUtil.gson.fromJson(response.body().string(),CodeResult.class);
                Log.d(TAG, "onResponse: "+codeResult.getMsg());
                if (codeResult.getRstCode()==200){
                    ModifyPetActivity.this.runOnUiThread(()->{
                        Toast.makeText(ModifyPetActivity.this.getApplicationContext(),"宠物上传成功",Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
                if (codeResult.getRstCode()==400){
                    ModifyPetActivity.this.runOnUiThread(()->{
                        Toast.makeText(ModifyPetActivity.this.getApplicationContext(),codeResult.getMsg(),Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });
    }

    private void getPet(String petID){
        Log.d(TAG, "getPet: "+petID);
        RequestBody requestBody=new FormBody.Builder()
                .add("petID",petID)
                .build();
        HttpUtil.sendPost("/pet",requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ModifyPetActivity.this.runOnUiThread(()->{
                    Toast.makeText(ModifyPetActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Pet pet= JsonUtil.gson.fromJson(response.body().string(),Pet.class);
                Log.d(TAG, "onResponse: "+pet.getName());
                runOnUiThread(()->{
                    nameEditTxt.setText(pet.getName());
                    detailEditTxt.setText(pet.getDetail());
                    varietyTxt.setText(pet.getVariety());
                    ageTxt.setText(pet.getAge());

                    if(pet.getSex().equals("雌")){
                        ((RadioButton)sexRg.getChildAt(0)).setChecked(true);
                    }
                    else if(pet.getSex().equals("雄")){
                        ((RadioButton)sexRg.getChildAt(1)).setChecked(true);
                    }
                    else{
                        ((RadioButton)sexRg.getChildAt(2)).setChecked(true);
                    }

                    healthTxt.setText(pet.getHealthStatus());
                    otherTxt.setText(pet.getOther());

                    Glide.with(ModifyPetActivity.this)
                            .load(pet.getImgURL())
                            .placeholder(R.drawable.ic_pets_black_24dp)
                            .error(R.drawable.ic_pets_black_24dp)
                            .into(petImgView);
                });
            }
        });
    }
}
