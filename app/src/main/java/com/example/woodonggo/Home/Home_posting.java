package com.example.woodonggo.Home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.woodonggo.R;

public class Home_posting extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imgView_close;
    EditText edt_title, edt_content;
    RadioGroup radioGroup;
    RadioButton btnGolf, btnBowling, btnPingpong;
    EditText edtTitle, edtContent;
    Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_posting);

        imgView_close = findViewById(R.id.imgView_close);
        toolbar = findViewById(R.id.toolbar);
        radioGroup = findViewById(R.id.radioGroup);
        btnGolf = findViewById(R.id.btnGolf);
        btnBowling = findViewById(R.id.btnBowling);
        btnPingpong = findViewById(R.id.btnPingpong);
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        btnPost = findViewById(R.id.btnPost);

        //라디오
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnGolf) {
                    btnGolf.setTextColor(Color.WHITE);
                    btnBowling.setTextColor(Color.DKGRAY);
                    btnPingpong.setTextColor(Color.DKGRAY);
                } else if (checkedId == R.id.btnBowling) {
                    btnBowling.setTextColor(Color.WHITE);
                    btnGolf.setTextColor(Color.DKGRAY);
                    btnPingpong.setTextColor(Color.DKGRAY);
                } else if (checkedId == R.id.btnPingpong) {
                    btnPingpong.setTextColor(Color.WHITE);
                    btnGolf.setTextColor(Color.DKGRAY);
                    btnBowling.setTextColor(Color.DKGRAY);
                }
            }
        });

        //이미지 뷰 'X'
        imgView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //게시글 올리기 버튼
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 에디트 텍스트 및 라디오 버튼이 선택되었다면 버튼색상(+텍스트) 변경 시키기
            }
        });
    }
}
