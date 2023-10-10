package com.example.woodonggo;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Mypage_interest_RecyclerView_Adapter extends RecyclerView.Adapter<Mypage_interest_RecyclerView_Adapter.ViewHolder> {

    private List<Model_interest> dataList; // 아이템 데이터 목록
    private OnItemClickListener listener; // 인터페이스 추가

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemDeleteClick(int position); // 아이템 삭제 이벤트를 처리하기 위한 콜백 메서드
    }

    // 생성자를 통해 데이터 목록을 받아옵니다.
    public Mypage_interest_RecyclerView_Adapter(List<Model_interest> dataList) {
        this.dataList = dataList;
    }

    // ViewHolder 클래스 정의
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImage;
        public TextView tvName;
        public TextView uploadTime;
        public TextView tvTitle;
        public View divider;
        public ImageButton moreBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tv_name);
            uploadTime = itemView.findViewById(R.id.upload_time);
            tvTitle = itemView.findViewById(R.id.tv_title);
            divider = itemView.findViewById(R.id.divider);
            moreBtn = itemView.findViewById(R.id.more_btn);

            moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v, getAdapterPosition());
                }
            });
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

    private void showPopupMenu(View view, int position) {
        if (listener != null && position != RecyclerView.NO_POSITION) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.mypage_interest_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menu_remove) {
                        listener.onItemDeleteClick(position);
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    }

    // setData 메서드 추가
    public void setData(List<Model_interest> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }
}
