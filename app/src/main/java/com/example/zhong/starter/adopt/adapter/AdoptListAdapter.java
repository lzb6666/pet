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

import com.example.zhong.starter.adopt.PetDetailActivity;
import com.example.zhong.starter.vo.Pet;

import java.util.ArrayList;
import java.util.List;


public class AdoptListAdapter extends RecyclerView.Adapter<AdoptListAdapter.ViewHolder> {
    private List<Pet> petList;
    private Context context;
    private String type;
    private static final String TAG="AdoptListAdapter";

    public AdoptListAdapter(List<Pet> petList) {
        this.petList = petList;
    }

    public AdoptListAdapter(Context context) {
        this.context=context;
        petList=new ArrayList<>();
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
        this.notifyDataSetChanged();
    }

    public void setType(String type){
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pet,viewGroup,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Pet pet=petList.get(i);
        viewHolder.petName.setText(pet.getName());
        viewHolder.variety.setText(pet.getVariety());
        Glide.with(context)
                .load(pet.getImgURL())
                .placeholder(R.drawable.ic_pets_black_24dp)
                .error(R.drawable.ic_pets_black_24dp)
                .into(viewHolder.petImage);
        viewHolder.petView.setOnClickListener(v -> {
            Intent intentToPetDetail = new Intent(context, PetDetailActivity.class);
            Log.d(TAG, "onCreateViewHolder: "+petList.get(i).getImgURL());
            intentToPetDetail.putExtra("petID",petList.get(i).getPetID());
            intentToPetDetail.putExtra("from",type);

            context.startActivity(intentToPetDetail);
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
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
