package com.example.jpushdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.test.xiao.songandroid.R;

public class WelcomeActivity extends AppCompatActivity {
    WebView webView;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            next();
        }
    };

    private void next() {
        Intent intent= new Intent(WelcomeActivity.this,HomeActivity.class);
        startActivity(intent);
        //关闭当前网页
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        webView = (WebView) findViewById(R.id.we_webview);
        loadWeb();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //调用子线程,不能操作UI,需要用到Handler切换到主线程
                //多个线程有目的解决有耗时任务,就会卡界面
                //用多个线程后,将耗时任务放到子线程,这样主线程(UI主线程)不会卡住
                handler.sendEmptyMessage(0);
            }
        },3000);
    }
    public void loadWeb() {//192.168.3.19:8080
        String url = "http://www.song724.cn/";
        //此方法可以在webview中打开链接而不会跳转到外部浏览器
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

}
