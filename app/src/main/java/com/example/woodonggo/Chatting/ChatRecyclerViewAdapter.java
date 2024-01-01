package com.example.woodonggo.Chatting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.woodonggo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder> {
    /*
  어댑터의 동작원리 및 순서
  1.(getItemCount) 데이터 개수를 세어 어댑터가 만들어야 할 총 아이템 개수를 얻는다.
  2.(getItemViewType)[생략가능] 현재 itemview의 viewtype을 판단한다
  3.(onCreateViewHolder)viewtype에 맞는 뷰 홀더를 생성하여 onBindViewHolder에 전달한다.
  4.(onBindViewHolder)뷰홀더와 position을 받아 postion에 맞는 데이터를 뷰홀더의 뷰들에 바인딩한다.
  */

    String TAG = "RecyclerViewAdapter";

    //리사이클러뷰에 넣을 데이터 리스트
    ArrayList<DataModelChat> dataModels;
    Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    MyViewHolder myViewHolder;

    //생성자를 통하여 데이터 리스트 context를 받음
    public ChatRecyclerViewAdapter(FragmentActivity context, ArrayList<DataModelChat> dataModels) {
        this.dataModels = dataModels;
        this.context = context;
    }

    public int getItemCount() {
        //데이터 리스트의 크기를 전달해주어야 함
        return dataModels.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");

        //자신이 만든 itemview를 inflate한 다음 뷰홀더 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_recyclerview_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        //생선된 뷰홀더를 리턴하여 onBindViewHolder에 전달한다.
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder");

        myViewHolder = (MyViewHolder)holder;

        myViewHolder.name_chat.setText(dataModels.get(position).getName());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatDetails.class);
                intent.putExtra("destUid", dataModels.get(position).getUserId());
                intent.putExtra("postingId", dataModels.get(position).getRoomId());
                context.startActivity(intent);
            }
        });

        String userId = dataModels.get(position).getUserId();

        //Log.d("userId", userId);
        if (userId != null && !userId.isEmpty()) {
            loadProfileImage(userId, myViewHolder.imageView);
        } else {
            // 사용자 아이디가 없는 경우에 대한 처리
            // 예: 기본 이미지를 표시하거나 에러 메시지를 출력하는 등
        }
        //myViewHolder.imageView.setImageResource(dataModels.get(position).getImg());
        myViewHolder.name_chat.setText(dataModels.get(position).getName());
        myViewHolder.chat_chat.setText(dataModels.get(position).getLastMessage());
    }

    private void loadProfileImage(String userId, ImageView imgProfile) {
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
                                        Log.d("img e", profileImageUrl);
                                        // 추가: 이미지 URL 및 사용자 이름이 유효한지 확인
                                        if (!profileImageUrl.isEmpty()) {
                                            // Glide를 사용하여 이미지 로드
                                            Glide.with(imgProfile.getContext())
                                                    .load(profileImageUrl)
                                                    .error(R.drawable.noprofile)
                                                    .into(myViewHolder.imageView);
                                        } else {
                                            // 이미지 URL 또는 사용자 이름이 유효하지 않은 경우에 대한 처리
                                            // 예: 기본 이미지를 표시하거나 에러 메시지를 출력하는 등
                                            Log.d("img error", "이미지 표시 에러");
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

    public void add(DataModelChat chatModel) {
        dataModels.add(chatModel);
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name_chat;
        TextView chat_chat;
        TextView add_chat;
        ImageView imageView;
        int img_source;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgChat);
            name_chat =  itemView.findViewById(R.id.nameChat);
            chat_chat = itemView.findViewById(R.id.chatContent);
            add_chat = itemView.findViewById(R.id.add_chat);
        }
    }
}
