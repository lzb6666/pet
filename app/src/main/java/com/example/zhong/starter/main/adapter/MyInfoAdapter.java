package com.example.zhong.starter.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhong.starter.R;
import com.example.zhong.starter.main.RecordDetailActivity;
import com.example.zhong.starter.vo.AdoptRecord;
import com.example.zhong.starter.vo.User;

import java.util.ArrayList;
import java.util.List;

public class MyInfoAdapter extends RecyclerView.Adapter<MyInfoAdapter.ViewHolder> {

    private List<String> titles;

    private List<String> contents;
    private Context context;

    private OnItemClickListener mItemClickListener;

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getContents() {
        return contents;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;

        TextView content;

        public ViewHolder(View view){
            super(view);
            title=view.findViewById(R.id.textView_title);
            content=view.findViewById(R.id.textView_content);
        }
    }

    public MyInfoAdapter(Context context){
        contents = new ArrayList<>();
        titles = new ArrayList<>();
        this.context = context;
    }

    public void setContents(User user){
        contents.clear();
        contents.add(user.getSex());
        contents.add(user.getAge());
        contents.add(user.getCareer());
        contents.add(user.getCity());
        contents.add(user.getPetExperience());
        contents.add(user.getPreference());

        this.notifyDataSetChanged();
    }

    public void setTitles(List<String> titles){
        this.titles=titles;
        this.notifyDataSetChanged();
    }

    public static interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_info,viewGroup,false);
        final RecordAdapter.ViewHolder viewHolder=new RecordAdapter.ViewHolder(view);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick((Integer) v.getTag());
            }
        });


        return new MyInfoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyInfoAdapter.ViewHolder viewHolder, int i) {
        String content = contents.get(i);
        if(content==null){
            content = "暂无信息";
        }
        viewHolder.title.setText(titles.get(i));

        viewHolder.content.setText(content);
        viewHolder.itemView.setTag(i);

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }
}
