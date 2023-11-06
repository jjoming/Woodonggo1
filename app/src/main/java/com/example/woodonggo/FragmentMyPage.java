package com.example.woodonggo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentMyPage extends Fragment {

    Button profile_btn;
    TextView textLikeList, textMatchList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);
        profile_btn = rootView.findViewById(R.id.profile_btn);
        textLikeList = rootView.findViewById(R.id.textLikeList);
        textMatchList = rootView.findViewById(R.id.textMatchList);

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Mypage_profile.class);
                    startActivity(intent);
            }
        });


        textLikeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Mypage_interest.class);
                startActivity(intent);
            }
        });

        textMatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Mypage_match.class);
                startActivity(intent);
            }
        });



        return rootView;
    }
}