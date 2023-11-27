package com.example.woodonggo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.woodonggo.Home.TeamModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Adapter_team_home extends RecyclerView.Adapter<Adapter_team_home.ViewHolder> {
    private ArrayList<TeamModel> teamDataList;
    // FirebaseStorage 객체 초기화
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    @NonNull
    @Override
    public Adapter_team_home.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(teamDataList.get(position));

        String userId = teamDataList.get(position).getUserId();
        loadProfileImage(userId, holder.img_profile);

    }

    private void loadProfileImage(String userId, ImageView imgProfile) {
        // Firebase Storage에서 프로필 이미지 다운로드
        StorageReference profileRef = storageRef.child("user_profiles/" + userId + ".jpg");

        // Glide를 사용하여 이미지 로드
        Glide.with(imgProfile.getContext())
                .load(profileRef) // 로딩 중에 표시될 이미지
                .error(R.drawable.noprofile)  // 에러 발생 시 표시될 이미지
                .into(imgProfile);
    }

    @Override
    public int getItemCount() {
        if(teamDataList == null) {
            return 0;
        }
        return teamDataList.size();
    }

    public void teamDataList(ArrayList<TeamModel> list){
        this.teamDataList = list;
        notifyDataSetChanged();
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



    class ViewHolder extends RecyclerView.ViewHolder { // 뷰홀더 클래스
        private TextView writer;
        private TextView upload_date;
        private TextView content;
        private TextView likes;
        private ImageView img_profile, more, img_category, img_like;
        private boolean isLiked = false;
        String writerId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            writer = itemView.findViewById(R.id.text_name);
            upload_date = itemView.findViewById(R.id.text_time);
            content = itemView.findViewById(R.id.text_content);
            img_profile = itemView.findViewById(R.id.img_profile);
            more = itemView.findViewById(R.id.more);
            img_category = itemView.findViewById(R.id.img_category);
            img_like = itemView.findViewById(R.id.img_like);
            img_like.setImageResource(R.drawable.selector_icon_heart);
            likes = itemView.findViewById(R.id.text_like);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // '더보기' 버튼을 클릭했을 때 showPopupMenu 메서드 호출
                    showPopupMenu(view);
                }
            });
            img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // '좋아요' 버튼을 클릭했을 때 토글 메서드 호출
                    toggleLike(writerId);
                }
            });
        }

        public void onBind(TeamModel item) {
            writer.setText(item.getWriter());
            // Timestamp를 String으로 변환
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(item.getUploadDate().toDate());
            upload_date.setText(dateString);
            content.setText(item.getContent());
            if (item.getSports().equals("탁구")) {
                Glide.with(img_category.getContext())
                        .load(R.drawable.icon_ping_pong)
                        .into(img_category);
                writerId = item.getWritingId();
            }
            likes.setText(String.valueOf(item.getLikesCount()));
        }

        private void toggleLike(String writerId) {
            isLiked = !isLiked; // 상태를 토글

            // 좋아요 수 업데이트
            updateLikesCount(writerId, isLiked);

            updateLikesCountInModel(writerId, isLiked);

            img_like.setSelected(isLiked);
        }

        private void updateLikesCount(String writerId, boolean isLiked) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // 좋아요 수를 1 또는 -1로 업데이트
            int incrementValue = isLiked ? 1 : -1;

            // Writing 컬렉션에서 해당 게시글의 좋아요 수를 업데이트
            db.collection("Writing")
                    .document(writerId)
                    .update("likesCount", FieldValue.increment(incrementValue))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // 성공적으로 업데이트된 경우
                            Log.d("Toggle Like", "Likes count updated successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 업데이트 실패 시
                            Log.e("Toggle Like", "Error updating likes count", e);
                        }
                    });
        }

        private void updateLikesCountInModel(String writerId, boolean isLiked) {
            for (TeamModel teamModel : teamDataList) {
                if (teamModel.getWritingId().equals(writerId)) {
                    teamModel.setLikesCount(isLiked ? teamModel.getLikesCount() + 1 : teamModel.getLikesCount() - 1);
                    break;
                }
            }

            // 어댑터에게 데이터 집합이 변경되었음을 알립니다.
            notifyDataSetChanged();
        }
    }
}