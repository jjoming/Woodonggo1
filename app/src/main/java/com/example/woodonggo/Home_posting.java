package com.example.woodonggo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Home_posting extends AppCompatActivity {
    Toolbar toolbar;

    ImageView imgView_close;
    EditText edt_title, edt_content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_posting);

        final Spinner spinner = findViewById(R.id.spinner);

        // 드롭다운 항목 배열
        String[] items = {"종목 선택", "농구", "팀_배드민턴", "야구", "족구", "축구", "골프", "당구", "개인_배드민턴", "볼링", "탁구"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String selectedItem = (String) parentView.getItemAtPosition(position);

                        if (!selectedItem.equals("종목 선택")) {
                            spinner.setBackgroundResource(R.drawable.spinner_custom_o);
                            textView.setTextColor(Color.WHITE);
                        } else {
                            spinner.setBackgroundResource(R.drawable.spinner_custom_x);
                            textView.setTextColor(Color.BLACK);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // 아무 것도 선택되지 않았을 때의 처리
                    }
                });
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //이미지 뷰 'X'
        imgView_close = findViewById(R.id.imgView_close);
        imgView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_posting.this, Fragment_home.class);
                startActivity(intent);
            }
        });

        edt_title = findViewById(R.id.edt_title);
        edt_content = findViewById(R.id.edt_content);
        toolbar = findViewById(R.id.toolbar);
    }
}