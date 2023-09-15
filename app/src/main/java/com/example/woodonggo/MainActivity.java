package com.example.woodonggo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    Fragment_home fragment_home;
    Fragment_ranking fragment_ranking;
    Fragment_place fragment_place;
    Fragment_chatting fragment_chatting;
    Fragment_myPage fragment_myPage;
    BottomNavigationView bottomNavi;
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






        fragment_home = new Fragment_home();
        fragment_ranking = new Fragment_ranking();
        fragment_place = new Fragment_place();
        fragment_chatting = new Fragment_chatting();
        fragment_myPage = new Fragment_myPage();

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
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_home()).commit();
                }

                return true;
            }
        });
    }
}