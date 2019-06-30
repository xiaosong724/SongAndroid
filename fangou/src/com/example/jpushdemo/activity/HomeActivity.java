package com.example.jpushdemo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.test.xiao.songandroid.R;

public class HomeActivity extends AppCompatActivity {
    WebView webView;
    TextView textView;
    TextView textViewdel;
    TextView textViewgame;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        webView = (WebView) findViewById(R.id.img_webview);
        textView = (TextView)findViewById(R.id.go_log);
        textViewdel = (TextView)findViewById(R.id.go_dellog);
        textViewgame = (TextView)findViewById(R.id.go_game);
        loadWeb();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                startActivity(intent);
            }
        });
        textViewdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DeleteActivity.class);
                startActivity(intent);
            }
        });
        textViewgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void loadWeb() {//192.168.3.19:8080
        String url = "http://www.song724.cn/imghome";
        //此方法可以在webview中打开链接而不会跳转到外部浏览器
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(url);
    }



    public void start(View view){
        if(mediaPlayer==null){
            ready();
        }
        mediaPlayer.start();
    }

    private void ready(){
        if(mediaPlayer==null){
            mediaPlayer= new MediaPlayer().create(this,R.raw.m1);
        }
    }

}

