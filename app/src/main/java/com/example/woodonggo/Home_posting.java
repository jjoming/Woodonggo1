package com.example.woodonggo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Home_posting extends AppCompatActivity {

    ImageView imgView_close;
    EditText edt_title, edt_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_posting);

        final Spinner spinner = findViewById(R.id.spinner);

        // 드롭다운 항목 배열
        String[] items = {"농구", "팀_배드민턴", "야구", "족구", "축구", "골프", "당구", "개인_배드민턴", "볼링", "탁구"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 것도 선택되지 않았을 때의 처리
            }
        });

        //이미지 뷰 'X'
        imgView_close = findViewById(R.id.imgView_close);
        imgView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Fragment_home.class);
                startActivity(intent);
            }
        });

        edt_title = findViewById(R.id.edt_title);
        edt_content = findViewById(R.id.edt_content);


    }
}
