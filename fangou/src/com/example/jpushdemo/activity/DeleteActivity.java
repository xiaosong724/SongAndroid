package com.example.jpushdemo.activity;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.test.xiao.songandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DeleteActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    private ListView listView;
    private String URL="http://www.song724.cn/apilogdelimg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        listView = (ListView) findViewById(R.id.tv_listlog);
        new NewsAsyncTask().execute(URL);
        //start(new View(this));
    }


    /**
     * 将URL对应的JSON格式数据转换所需的Logimg对象
     * @param url
     * @return
     */
    private List<Logimg>getJsonData(String url){
        List<Logimg> logimgs = new ArrayList<>();
        try {
            String jsonString=readStream(new URL(url).openStream());
           // Log.i("sxs",jsonString);
            JSONObject jsonObject;

            Logimg logimg;

                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject= jsonArray.getJSONObject(i);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject=jsonArray.getJSONObject(i);
                    logimg = new Logimg();
                    logimg.setTitle(jsonObject.getString("title"));
                    String imgpa=jsonObject.getString("imgpath");
                    if(imgpa.indexOf(".mp4")!=-1||imgpa.indexOf(".avi")!=-1||imgpa.indexOf(".rmvb")!=-1){
                        imgpa= imgpa.substring(0, imgpa.lastIndexOf(".")) + ".jpg";
                        logimg.setImgpath(imgpa);
                    }else {
                        logimg.setImgpath(imgpa);
                    }
                    logimg.setImgid(Integer.parseInt(jsonObject.getString("imgid")));
                    logimg.setLogid(Integer.parseInt(jsonObject.getString("logid")));
                    logimg.setDatetime(jsonObject.getString("datetime"));
                    logimg.setFiletype(jsonObject.getString("filetype"));
                    logimgs.add(logimg);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return logimgs;
    }

    /**
     * 通过流解析网页返回的数据
     * @param is
     * @return
     */
    private String readStream(InputStream is){
        InputStreamReader isr;
        String result="";
        try {
            String line="";
            isr= new InputStreamReader(is,"utf-8");
            BufferedReader bf= new BufferedReader(isr);
            while ((line=bf.readLine())!=null){
                result+=line;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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

    /**
     * 实现网络的异步访问
     */
    class NewsAsyncTask extends AsyncTask<String,Void,List<Logimg>>{

        @Override
        protected List<Logimg> doInBackground(String... strings) {
            return getJsonData(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Logimg> logimgs) {
            super.onPostExecute(logimgs);
            LogimgAdapter adapter= new LogimgAdapter(DeleteActivity.this,logimgs,listView);
            listView.setAdapter(adapter);
        }
    }



}
