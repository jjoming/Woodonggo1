package com.example.woodonggo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_notify_like extends RecyclerView.Adapter<Adapter_notify_like.ViewHolder> {
    private ArrayList<String> localDataSet;
    private boolean isImageViewVisible = true;

    public Adapter_notify_like(ArrayList<String> dataSet) {
        this.localDataSet = dataSet;
    }

    // 이미지뷰를 보이게 하는 메서드
    public void showImageView() {
        isImageViewVisible = true;
        notifyDataSetChanged();
    }

    // 이미지뷰를 숨기는 메서드
    public void hideImageView() {
        isImageViewVisible = false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_like_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = localDataSet.get(position);
        holder.text_name.setText(item);

        if (isImageViewVisible) {
            holder.img_x.setVisibility(View.VISIBLE);
        } else {
            holder.img_x.setVisibility(View.GONE);
        }

        // 나머지 뷰 요소 설정
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text_name, text_time;
        private ImageView imgView;
        public ImageView img_x;

        public ViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_time = itemView.findViewById(R.id.text_time);
            imgView = itemView.findViewById(R.id.imgView);
            img_x = itemView.findViewById(R.id.img_x);
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