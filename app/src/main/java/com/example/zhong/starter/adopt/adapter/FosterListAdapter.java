package com.example.zhong.starter.adopt.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhong.starter.R;
import com.example.zhong.starter.adopt.NurseDetailActivity;
import com.example.zhong.starter.vo.Nurse;

import java.util.ArrayList;
import java.util.List;


public class FosterListAdapter extends RecyclerView.Adapter<FosterListAdapter.ViewHolder> {
    private List<Nurse> nurseList;
    private Context context;
    private String type;
    private static final String TAG="AdoptListAdapter";

    public FosterListAdapter(List<Nurse> petList) {
        this.nurseList = petList;
    }

    public FosterListAdapter(Context context) {
        this.context=context;
        nurseList=new ArrayList<>();
    }

    public List<Nurse> getNurseList() {
        return nurseList;
    }

    public void setNurseList(List<Nurse> nurseList) {
        this.nurseList = nurseList;
        this.notifyDataSetChanged();
    }

    public void setType(String type){
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pet,viewGroup,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Nurse pet = nurseList.get(i);
        viewHolder.petName.setText(pet.getName());
        viewHolder.variety.setText(pet.getVariety());
        Glide.with(context)
                .load(pet.getImgURL())
                .placeholder(R.drawable.ic_pets_black_24dp)
                .error(R.drawable.ic_pets_black_24dp)
                .into(viewHolder.petImage);
        viewHolder.petView.setOnClickListener(v -> {
            Intent intentToNurseDetail = new Intent(context, NurseDetailActivity.class);
            Log.d(TAG, "onCreateViewHolder: "+nurseList.get(i).getImgURL());
            intentToNurseDetail.putExtra("nurseID",nurseList.get(i).getNurseID());
            intentToNurseDetail.putExtra("from",type);

            context.startActivity(intentToNurseDetail);
        });
    }

    @Override
    public int getItemCount() {
        return nurseList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView petImage;
        TextView petName;
        TextView variety;
        View petView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            petView=itemView;
            petImage=itemView.findViewById(R.id.imgView_petList_img);
            petName=itemView.findViewById(R.id.txtView_petList_name);
            variety=itemView.findViewById(R.id.txtView_petList_variety);
        }
    }
}
