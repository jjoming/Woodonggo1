package com.example.woodonggo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_home_category2 extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    String[] categoryName;
    int[] arrCategoryImg;

    public Adapter_home_category2(Context context, String[] categoryName, int[] categoryImg) {
        this.context = context;
        this.categoryName = categoryName;
        this.arrCategoryImg = categoryImg;
    }

    @Override
    public int getCount() {
        return categoryName.length;
    }

    @Override
    public Object getItem(int position) {
        return categoryName[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null) {
            view = inflater.inflate(R.layout.gridview_category_item, null);
        }
        ImageView img = view.findViewById(R.id.imgView);
        TextView text = view.findViewById(R.id.text);

        img.setImageResource(arrCategoryImg[position]);
        text.setText(categoryName[position]);

        return view;
    }
}
