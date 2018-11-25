package com.example.zhong.starter.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhong.starter.R;

import java.util.ArrayList;

public class MineAdapter extends BaseAdapter {

    private ArrayList<String> items;
    private ArrayList<Integer> icons;
    private Context context;

    public MineAdapter(ArrayList<String> items, ArrayList<Integer> icons, Context context) {
        this.items = items;
        this.icons = icons;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mine, parent,false);
        }

        final ImageView imageViewIcon=convertView.findViewById(R.id.imageView_custom_list_item);
        imageViewIcon.setImageResource(icons.get(position));

        final TextView textViewContent=convertView.findViewById(R.id.textView_custom_list_item);
        textViewContent.setText(items.get(position));

        return convertView;
    }
}
