package com.example.woodonggo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_myPage extends Fragment {

    Button profile_btn;
    LinearLayout mypage_interest_layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);
        Button profile_btn = rootView.findViewById(R.id.profile_btn);
        LinearLayout mypage_interest_layout = rootView.findViewById(R.id.mypage_interest_layout);


        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Mypage_profile.class);
                    startActivity(intent);
            }
        });

        mypage_interest_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Mypage_interest.class);
                startActivity(intent);
            }
        });



        return rootView;
    }
}