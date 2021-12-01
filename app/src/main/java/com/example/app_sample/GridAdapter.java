package com.example.app_sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_sample.models.Category;
import com.example.app_sample.utils.Utils;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Category> categories;

    public GridAdapter(Context context) {
        this.context = context;
        this.categories = Utils.getCategories();
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        ImageView img = convertView.findViewById(R.id.img_dish);
        TextView name = convertView.findViewById(R.id.category_name);
        img.setImageResource(getItem(position).getImgId());
        name.setText(getItem(position).getName());
        return convertView;
    }
}
