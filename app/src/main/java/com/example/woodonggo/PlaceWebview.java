package com.example.woodonggo;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceWebview  extends AppCompatActivity {
    String title, url;
    TextView webText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_webview);

        WebView webView = findViewById(R.id.webView);
        webText = findViewById(R.id.webText);

        Intent inIntent = getIntent();
        title = inIntent.getStringExtra("title");
        url = inIntent.getStringExtra("url");

        webText.setText(title);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);
    }
}
