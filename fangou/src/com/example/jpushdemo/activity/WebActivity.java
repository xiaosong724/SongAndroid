package com.example.jpushdemo.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.test.xiao.songandroid.R;

public class WebActivity extends AppCompatActivity {
    WebView webView;
    TextView textViewhome;
    private final static int  REQUEST_SELECT_FILE = 2;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> uploadMessage; //多选文件回调
    private ValueCallback<Uri> mUploadMessage ; //单选文件回调




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        textViewhome = (TextView)findViewById(R.id.go_game);
        webView = (WebView) findViewById(R.id.wv_webview);
        textViewhome = (TextView)findViewById(R.id.go_home);
        loadWeb();

        textViewhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void loadWeb() {//192.168.3.19:8080
        String url = "http://www.song724.cn/home";
        //此方法可以在webview中打开链接而不会跳转到外部浏览器
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
        //设置可以访问文件
        ws.setAllowFileAccess(true);
        //设置支持缩放
        ws.setBuiltInZoomControls(true);
        //去掉缩放按钮
        ws.setDisplayZoomControls(false);
        webView.loadUrl(url);

        webView.setWebChromeClient(new WebChromeClient() {


            // For 3.0+ Devices (Start)
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            // For Lollipop 5.0+ Devices 目测应该5.0+才支持多选
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
                uploadMessage = filePathCallback;
                Intent intent = fileChooserParams.createIntent();

                //此处增加参数  允许多选文件
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(getBaseContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            //for Android <3.0
            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });

    }
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null || intent == null) {
                    //若没有选择文件就回退  执行清空  不进行这步操作无法再次选择文件
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                    return;
                }
                //由于用户可能单选或多选   单选getData  多选时getClipData()
                if (intent.getData() != null) {
                    uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                } else if (intent.getClipData() != null) {
                    Uri[] uris = new Uri[intent.getClipData().getItemCount()];
                    for (int i = 0; i < intent.getClipData().getItemCount(); i++) {
                        uris[i] = intent.getClipData().getItemAt(i).getUri();
                    }
                    uploadMessage.onReceiveValue(uris);
                }
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage || intent == null) {
                //若没有选择文件就回退  执行清空  不进行这步操作无法再次选择文件
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
                return;
            }
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getBaseContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onKeyDown ( int keyCode, KeyEvent event){
        //重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
