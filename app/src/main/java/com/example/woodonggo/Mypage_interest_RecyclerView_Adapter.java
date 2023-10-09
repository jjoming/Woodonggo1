package com.example.woodonggo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Mypage_interest_RecyclerView_Adapter extends RecyclerView.Adapter<Mypage_interest_RecyclerView_Adapter.ViewHolder> {

    private List<Model_interest> dataList; // 아이템 데이터 목록

    // 생성자를 통해 데이터 목록을 받아옵니다.
    public Mypage_interest_RecyclerView_Adapter(List<Model_interest> dataList) {
        this.dataList = dataList;
    }

    // ViewHolder 클래스 정의
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImage;
        public TextView tvName;
        public TextView uploadTime;
        public TextView tvTitle;
        public View divider;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tv_name);
            uploadTime = itemView.findViewById(R.id.upload_time);
            tvTitle = itemView.findViewById(R.id.tv_title);
            divider = itemView.findViewById(R.id.divider);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아이템 뷰 생성 및 ViewHolder 반환
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_interest_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 아이템 데이터를 ViewHolder에 바인딩
        Model_interest item = dataList.get(position);

        // 아이템의 데이터를 뷰에 설정
        holder.profileImage.setImageResource(item.getProfileImageResource());
        holder.tvName.setText(item.getName());
        holder.uploadTime.setText(item.getUploadTime());
        holder.tvTitle.setText(item.getTitle());

        // 마지막 아이템의 경우 구분선을 숨김
        if (position == dataList.size() - 1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
