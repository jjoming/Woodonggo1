package com.example.woodonggo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import com.example.woodonggo.Chatting.FragmentChatting;
import com.example.woodonggo.Raingking.FragmentRanking;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    FragmentHome fragment_home;
    FragmentRanking fragment_ranking;
    FragmentPlace fragment_place;
    FragmentChatting fragment_chatting;
    FragmentMyPage fragment_myPage;
    BottomNavigationView bottomNavi;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //키 해시 얻기
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("키해시는 :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Intent inIntent = getIntent();
        userId = inIntent.getStringExtra("userId2");


        fragment_home = new FragmentHome();
        fragment_ranking = new FragmentRanking();
        fragment_place = new FragmentPlace();
        fragment_chatting = new FragmentChatting();
        fragment_myPage = new FragmentMyPage();

        bottomNavi = findViewById(R.id.bottomNavi);
        // 초기 화면으로 Fragment_home을 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment_home).commit();

        //BottomNavi 처리
        bottomNavi.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_ranking) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment_ranking).commit();
                } else if (itemId == R.id.menu_place) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment_place).commit();
                } else if (itemId == R.id.menu_chatting) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment_chatting).commit();
                } else if (itemId == R.id.menu_mypage) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment_myPage).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
                }

                return true;
            }
        });
    }
}