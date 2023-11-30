package com.example.woodonggo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.example.woodonggo.Home.Home_Fragment_Personal;
import com.example.woodonggo.Home.Home_posting_detail;
import com.example.woodonggo.Home.PersonalModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Adapter_personal_home extends RecyclerView.Adapter<Adapter_personal_home.ViewHolder> {
    //리사이클러뷰에 넣을 데이터 리스트
    private ArrayList<PersonalModel> personalDataList;
    private Context context;

    // FirebaseStorage 객체 초기화
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public  Adapter_personal_home(Context context, ArrayList<PersonalModel> personalDataList){
        this.context = context;
        this.personalDataList = personalDataList;
    }
    @NonNull
    @Override
    public Adapter_personal_home.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(personalDataList.get(position));

        String userId = personalDataList.get(position).getUserId();
        if (userId != null && !userId.isEmpty()) {
            loadProfileImage(userId, holder.img_profile, holder.writer);
        } else {
            // 사용자 아이디가 없는 경우에 대한 처리
            // 예: 기본 이미지를 표시하거나 에러 메시지를 출력하는 등
        }
    }

    private void loadProfileImage(String userId, ImageView imgProfile, TextView writer) {
        if (userId != null && !userId.isEmpty()) {
            // Firebase Storage에서 사용자 프로필 이미지 가져오기
            StorageReference profileImageRef = storageRef.child("user_profiles/" + userId + ".jpg");

            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // 사용자 프로필 이미지 다운로드 URL을 얻은 경우
                    String profileImageUrl = uri.toString();

                    // Firebase Firestore에서 사용자 정보 가져오기
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("User")
                            .document(userId)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        // 사용자 정보가 존재할 경우 프로필 이미지 및 이름 업데이트
                                        String userName = documentSnapshot.getString("name");

                                        // 추가: 이미지 URL 및 사용자 이름이 유효한지 확인
                                        if (!profileImageUrl.isEmpty() && writer != null) {
                                            // Glide를 사용하여 이미지 로드
                                            Glide.with(imgProfile.getContext())
                                                    .load(profileImageUrl)
                                                    .error(R.drawable.noprofile)
                                                    .into(imgProfile);

                                            // 작성자 이름 업데이트
                                            writer.setText(userName);
                                        } else {
                                            // 이미지 URL 또는 사용자 이름이 유효하지 않은 경우에 대한 처리
                                            // 예: 기본 이미지를 표시하거나 에러 메시지를 출력하는 등
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // 에러 처리
                                    Log.e("Profile Update", "Error fetching user profile", e);
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // 프로필 이미지 다운로드 URL을 얻지 못한 경우 또는 다운로드 실패한 경우
                    // 예: 기본 이미지를 표시하거나 에러 메시지를 출력하는 등
                    Log.e("Profile Update", "Error fetching profile image URL", e);
                }
            });
        } else {
            // userId가 null이거나 비어 있는 경우에 대한 처리
            // 예: 기본 이미지를 표시하거나 에러 메시지를 출력하는 등
        }
    }

    @Override
    public int getItemCount() {
        if(personalDataList == null) {
            return 0;
        }
        return personalDataList.size();
    }

    public void personalDataList(ArrayList<PersonalModel> list){
        this.personalDataList = list;
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

            //리사이클러뷰 아이템 클릭 이벤트 처리
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context, Home_posting_detail.class);
                        intent.putExtra("name", personalDataList.get(pos).getWriter());
                        intent.putExtra("content", personalDataList.get(pos).getContent());
                        intent.putExtra("uploadDate", personalDataList.get(pos).getUploadDate());
                        intent.putExtra("likeCount", personalDataList.get(pos).getLikesCount());
                        context.startActivity(intent);
                    }
                }
            });

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

        public void onBind(PersonalModel item) {
            writer.setText(item.getWriter());
            // Timestamp를 String으로 변환
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(item.getUploadDate().toDate());
            upload_date.setText(dateString);
            content.setText(item.getContent());
            if (item.getSports().equals("골프")) {
                Glide.with(img_category.getContext())
                        .load(R.drawable.icon_golf)
                        .into(img_category);
                writerId = item.getWritingId();
            } else if (item.getSports().equals("볼링")) {
                Glide.with(img_category.getContext())
                        .load(R.drawable.icon_bowling)
                        .into(img_category);
                writerId = item.getWritingId();
            }
            likes.setText(String.valueOf(item.getLikesCount()));
            // 사용자 프로필 이미지 업데이트
            loadProfileImage(item.getUserId(), img_profile, writer);
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
            for (PersonalModel personalModel : personalDataList) {
                if (personalModel.getWritingId().equals(writerId)) {
                    personalModel.setLikesCount(isLiked ? personalModel.getLikesCount() + 1 : personalModel.getLikesCount() - 1);
                    break;
                }
            }

            // 어댑터에게 데이터 집합이 변경되었음을 알립니다.
            notifyDataSetChanged();
        }
    }

    public void updateItem(int position) {
        notifyItemChanged(position);
    }

}