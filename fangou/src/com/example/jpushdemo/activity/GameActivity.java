package com.example.jpushdemo.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.test.xiao.songandroid.R;

public class GameActivity extends AppCompatActivity {
    WebView gamewebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gamewebview = (WebView) findViewById(R.id.game_webview);
        loadWeb();
    }
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void loadWeb() {//192.168.3.19:8080
        String url = "http://www.song724.cn/showgame";
        //此方法可以在webview中打开链接而不会跳转到外部浏览器
        WebSettings ws = gamewebview.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setAllowFileAccess(true);
        gamewebview.setWebViewClient(new WebViewClient());
        gamewebview.setWebChromeClient(new WebChromeClient());
        gamewebview.loadUrl(url);
    }
}
