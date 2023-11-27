package com.example.woodonggo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FragmentMyPage extends Fragment {

    Button profile_btn;
    TextView profileName, textLikeList, textMatchList;
    private String UserId;
    private StorageReference storageReference;
    ImageView profile;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // storageReference 초기화
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private View rootView;  // Variable to store the root view
    private Context context;
    String userId;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mypage, container, false);
        context = rootView.getContext();
        profile_btn = rootView.findViewById(R.id.profile_btn);
        textLikeList = rootView.findViewById(R.id.textLikeList);
        textMatchList = rootView.findViewById(R.id.textMatchList);
        profileName = rootView.findViewById(R.id.profileName);
        profile = rootView.findViewById(R.id.profileImg);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        userId = preferences.getString("userId", "");


        fetchUserName(userId);
        storageReference = storage.getReference();

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(requireContext(), Mypage_profile.class);
                    startActivity(intent);
            }
        });


        textLikeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MypageInterest.class);
                startActivity(intent);
            }
        });

        textMatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), Mypage_match.class);
                startActivity(intent);
            }
        });



        return rootView;
    }

    private void fetchUserName(String userId) {
        if(userId != null && !userId.isEmpty()) {
            DocumentReference userRef = db.collection("User").document(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // 사용자가 Firestore의 users 컬렉션에 존재하는 경우
                        String userName = document.getString("name");
                        // userName을 사용할 수 있습니다.
                        profileName.setText(userName);
                        fetchUserProfileImage(userId);
                    }
                } else {
                    // Firestore에서 사용자 확인 중 오류 발생
                    // 오류 처리를 수행하거나 필요에 따라 처리
                }
            });
        }

    }

    private void fetchUserProfileImage(String userId) {
        // StorageReference를 통해 프로필 이미지 파일에 대한 참조 획득
        StorageReference profileImageRef = storageReference.child("user_profiles/" + userId + ".jpg");

        // 이미지 다운로드 URL을 가져와서 Glide를 사용하여 이미지를 표시
        profileImageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // 성공적으로 다운로드 URL을 얻은 경우
                    Glide.with(context)
                            .load(uri)
                            .into(profile);
                    Log.d("TAG",requireContext().toString());
                })
                .addOnFailureListener(exception -> {
                    // 다운로드 URL을 얻는 중 오류가 발생한 경우 또는 이미지가 없는 경우
                    // 오류 처리를 수행하거나 기본 이미지를 표시하거나 필요에 따라 처리
                    // 예: Glide를 사용하여 기본 이미지를 표시
                    Glide.with(context)
                            .load(R.drawable.noprofile)
                            .into(profile);
                });
    }

}