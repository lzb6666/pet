package com.example.zhong.starter.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhong.starter.R;
import com.example.zhong.starter.adopt.PetDetailActivity;
import com.example.zhong.starter.main.RecordDetailActivity;
import com.example.zhong.starter.vo.AdoptRecord;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<AdoptRecord> records;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTxtView;
        ImageView petImgView;
        TextView timeTxtView;
        View recordView;
        public ViewHolder(View view){
            super(view);
            nameTxtView=view.findViewById(R.id.txtView_record_name);
            petImgView=view.findViewById(R.id.imgView_record);
            timeTxtView=view.findViewById(R.id.txtView_record_time);
            recordView=view.findViewById(R.id.item_record_view);
        }
    }

     public RecordAdapter(Context context){
        records=new ArrayList<>();
        this.context=context;
     }

     public void setRecords(List<AdoptRecord> records){
        this.records=records;
        this.notifyDataSetChanged();
     }

    @NonNull
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_record,viewGroup,false);
        final RecordAdapter.ViewHolder viewHolder=new RecordAdapter.ViewHolder(view);
        return new RecordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder viewHolder, int i) {
        AdoptRecord record=records.get(i);
        viewHolder.nameTxtView.setText(record.getPetName());
        viewHolder.timeTxtView.setText(record.getAdoptTime());
        Glide.with(context)
                .load(record.getImgURL())
                .placeholder(R.drawable.ic_pets_black_24dp)
                .error(R.drawable.ic_pets_black_24dp)
                .into(viewHolder.petImgView);
        viewHolder.recordView.setOnClickListener(v -> {
            Intent intentToPetDetail = new Intent(context, RecordDetailActivity.class);
            intentToPetDetail.putExtra("recordID",records.get(i).getRecordID());
            context.startActivity(intentToPetDetail);
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

}
