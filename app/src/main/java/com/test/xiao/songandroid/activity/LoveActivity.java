package com.test.xiao.songandroid.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.test.xiao.songandroid.R;

public class LoveActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        start(new View(this));
    }
    public void start(View view){
        if(mediaPlayer==null){
            ready2();
        }
        mediaPlayer.start();
    }
    private void ready2(){
        if(mediaPlayer==null){
            mediaPlayer= new MediaPlayer().create(this,R.raw.m2);
        }
    }



}
