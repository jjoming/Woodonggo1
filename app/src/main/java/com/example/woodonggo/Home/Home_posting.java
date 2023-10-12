package com.example.woodonggo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Home_posting extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imgView_close;
    EditText edt_title, edt_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_posting);

        final Spinner spinner = findViewById(R.id.spinner);
        imgView_close = findViewById(R.id.imgView_close);
        edt_title = findViewById(R.id.edt_title);
        edt_content = findViewById(R.id.edt_content);
        toolbar = findViewById(R.id.toolbar);

        // 드롭다운 항목 배열
        String[] items = {"종목 선택", "농구", "팀_배드민턴", "야구", "족구", "축구", "골프", "당구", "개인_배드민턴", "볼링", "탁구"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        final String selectedItem = (String) parentView.getItemAtPosition(position);

                        if (!selectedItem.equals("종목 선택")) { //종목을 선택했을 때
                            spinner.setBackgroundResource(R.drawable.spinner_custom_o);
                            textView.setTextColor(Color.WHITE);
                        }
                        else { //종목을 선택하지 않았을 때
                            spinner.setBackgroundResource(R.drawable.spinner_custom_x);
                            textView.setTextColor(Color.BLACK);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }
                });
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //이미지 뷰 'X'
        imgView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                int titleLen = edt_title.getText().length();
                int contentLen = edt_content.getText().length();

                //TODO 종목 선택이 안됐을 때의 경우
                if (itemId == R.id.upload) {
                    if (titleLen == 0 && contentLen == 0){ //게시글 제목과 내용 모두 작성하지 않았을 때
                        Toast.makeText(getApplicationContext(), "게시글 제목과 내용을 작성해주세요!", Toast.LENGTH_SHORT).show();
                    }
                    else if(edt_content.getText().length() == 0){ //게시글 내용을 작성하지 않았을 때
                        Toast.makeText(getApplicationContext(), "게시글 내용을 작성해주세요!", Toast.LENGTH_SHORT).show();
                    }
                    else if (edt_title.getText().length() == 0) { //게시글 제목을 작성하지 않았을 때
                        Toast.makeText(getApplicationContext(), "게시글 제목을 작성해주세요!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //TODO 게시글 올리기
                    }

                }
                return false;
            }
        });
    }
}
