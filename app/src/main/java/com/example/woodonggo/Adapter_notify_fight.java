package com.example.woodonggo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_notify_fight extends RecyclerView.Adapter<Adapter_notify_fight.ViewHolder> {
    private ArrayList<String> localDataSet;

    public Adapter_notify_fight(ArrayList<String> dataSet) {
        this.localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_fight_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = localDataSet.get(position);
        holder.text_name.setText(item);
        // 나머지 뷰 요소 설정
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text_name, text_time;
        private ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_time = itemView.findViewById(R.id.text_time);
            imgView = itemView.findViewById(R.id.imgView);
        }

        public TextView getText_name() {
            return text_name;
        }

        public TextView getText_time() {
            return text_time;
        }

        public ImageView getImgView() {
            return imgView;
        }
    }
}