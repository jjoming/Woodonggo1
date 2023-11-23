package com.example.woodonggo;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_home extends RecyclerView.Adapter<Adapter_home.ViewHolder> {
    private ArrayList<String> localDataSet;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = localDataSet.get(position);
        holder.text_name.setText(text);

        // more ImageView에 클릭 리스너 추가
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    public Adapter_home(ArrayList<String> dataSet) {
        localDataSet = dataSet;
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.home_more_menu, popupMenu.getMenu());

        //팝업 메뉴 아이템 클릭 이벤트
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.report_author) {
                    //TODO: 작성자 신고
                }
                else if(itemId == R.id.report_post) {
                    //TODO: 게시글 신고
                }
                else {
                    //TODO: 채팅 걸기
                }
                return false;
            }
        });
        popupMenu.show(); //팝업 메뉴 띄우기
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder { // 뷰홀더 클래스
        private TextView text_name, text_time, text_content, text_like;
        private ImageView img_profile, more, img_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_name);
            text_time = itemView.findViewById(R.id.text_time);
            text_content = itemView.findViewById(R.id.text_content);
            text_like = itemView.findViewById(R.id.text_like);
            img_profile = itemView.findViewById(R.id.img_profile);
            more = itemView.findViewById(R.id.more);
            img_category = itemView.findViewById(R.id.img_category);
        }

        public TextView getText_name() {
            return text_name;
        }

        public TextView getText_time() {
            return text_time;
        }

        public TextView getText_content() {
            return text_content;
        }

        public TextView getText_like() {
            return text_like;
        }

        public ImageView getImg_profile() {
            return img_profile;
        }

        public ImageView getImg_category() {
            return img_category;
        }
    }
}
