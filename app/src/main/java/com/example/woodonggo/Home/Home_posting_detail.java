package com.example.woodonggo.Home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.woodonggo.R;

public class Home_posting_detail extends AppCompatActivity {

    Toolbar toolbar;
    TextView textName, textContent, textTime, textLike;
    ImageView imgMore;
    String id, content, time, likeCount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_posting_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textName = findViewById(R.id.textName); //사용자 아이디
        textContent = findViewById(R.id.textContent); //게시글 내용
        textTime = findViewById(R.id.textTime); //게시글 업로드 시간
        textLike = findViewById(R.id.text_like); //좋아요 숫자
        imgMore = findViewById(R.id.more); //더보기 아이콘

        Intent intent = getIntent();
        id = intent.getExtras().getString("name");
        content = intent.getExtras().getString("content");
        time = intent.getExtras().getString("uploadDate");
        likeCount = intent.getExtras().getString("likeCount");

        textName.setText(id);
        textContent.setText(content);
        textTime.setText(time);
        textLike.setText(likeCount);

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '더보기' 버튼을 클릭했을 때 showPopupMenu 메서드 호출
                showPopupMenu(view);
            }
        });
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
