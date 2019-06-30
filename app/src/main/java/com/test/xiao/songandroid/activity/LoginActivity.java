package com.test.xiao.songandroid.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.test.xiao.songandroid.R;

import static android.app.Notification.VISIBILITY_SECRET;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    private int a=1;
    private int b=1;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "评论消息";
            int importance = NotificationManager.IMPORTANCE_MAX;
            createNotificationChannel(channelId, channelName, importance);

            channelId = "subscribe";
            channelName = "日志消息";
            importance = NotificationManager.IMPORTANCE_MAX;
            createNotificationChannel(channelId, channelName, importance);
        }
        Button Login = (Button) findViewById(R.id.login);
        Button luntan = (Button) findViewById(R.id.luntan);
        username = (EditText) findViewById(R.id.username);
        //password = (EditText) findViewById(R.id.password);

        luntan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),WebActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //post();
                shouLove();
            }
        });
     


    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        // 配置通知渠道的属性
        // 设置通知出现时的闪灯（如果 android 设备支持的话）
        channel.enableLights(true);
        channel.setLockscreenVisibility(VISIBILITY_SECRET);
        channel.setLightColor(Color.RED);
        channel.canShowBadge();//显示角标
        channel.enableVibration(true);
        // 自定义声音
        channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/raw/m1"),null);
        // 设置通知出现时的震动（如果 android 设备支持的话）
        channel.setVibrationPattern(new long[]{100, 200, 300});
        notificationManager.createNotificationChannel(channel);
    }
    public void sendChatMsg(View view) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle("收到一条评论消息")
                .setContentText("今天中午吃什么1111？")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.heying)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.heying))
                .setAutoCancel(true)
                .build();
        manager.notify(a++, notification);
    }
    public static void init(Context context){
    }
    public void sendSubscribeMsg(View view) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "subscribe")
                .setContentTitle("收到一条日志消息")
                .setContentText("地铁沿线30万商铺抢购中！")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.heying)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.heying))
                .setAutoCancel(true)
                .build();

        manager.notify(b++, notification);
    }

    public void start(View view){
        if(mediaPlayer==null){
            ready();
        }
        mediaPlayer.start();
    }
    private void shouLove() {
        String wen = username.getText().toString();
        if("我是繁狗我最乖".equals(wen)){
            Intent intent = new Intent(getApplicationContext(), LoveActivity.class);
            startActivity(intent);
        }else {
            start(new View(this));
            Toast.makeText(getApplicationContext(), "繁狗,不听话哦~", Toast.LENGTH_SHORT).show();
        }
    }
//    private void postAsynHttp() {
//        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody formBody = new FormBody.Builder()
//                .add("username", username.getText().toString()).add("password", password.getText().toString())
//                .build();
//        Request request = new Request.Builder()
//                .url("http://192.168.3.7:8080/loginapp")
//                .post(formBody)
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String str = response.body().string();
//                Log.i("wangshu", str);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (str.equals("登陆成功")) {
////                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                            startActivity(intent);
//                        } else {
//                            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//            }
//        });
//    }

    private void ready(){
        if(mediaPlayer==null){
            mediaPlayer= new MediaPlayer().create(this,R.raw.m1);
        }
    }

}

