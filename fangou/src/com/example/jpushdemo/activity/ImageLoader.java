package com.example.jpushdemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.test.xiao.songandroid.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;


public class ImageLoader {
    private ImageView mimageView;
    private String murl;
    private ListView mlistView;
    private Set<NewsimgAsyncTask> mTask;
    //创建缓存,底层hashmap
    private LruCache<String,Bitmap> mCaches;

    public ImageLoader(ListView listView){
        mlistView=listView;
        mTask= new HashSet<>();
        //获取最大可用内存
        int maxMemory =(int) Runtime.getRuntime().maxMemory();
        int cacheSize= maxMemory/4;
        mCaches=new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }
    //增加到缓存,判断是否存在
    public void addBitmapToCache(String  url,Bitmap bitmap){
        if(getBitmapFromCache(url)==null){
            mCaches.put(url,bitmap);
        }
    }
    //从缓存中获取数据
    public Bitmap getBitmapFromCache(String url){

        return mCaches.get(url);
    }
    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mimageView.getTag().equals(murl)){
                mimageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };


    public void showImageByThred(ImageView imageView,final String url){

            mimageView=imageView;
            murl=url;
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    Bitmap bitmap= getBitmapfromurl(url);
                    Message message=Message.obtain();
                    message.obj=bitmap;
                    handler.sendMessage(message);
                }
            }.start();


    }
    public Bitmap getBitmapfromurl(String urlstring){
        Bitmap bitmap;
        InputStream is=null;
        try {
            URL url= new URL(urlstring);
            HttpURLConnection connection= (HttpURLConnection)url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap=BitmapFactory.decodeStream(is);
            connection.disconnect();

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is!=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public void showImageByAsyncTask(ImageView imageView,String url){
        //从缓存中取图片
        Bitmap bitmap=getBitmapFromCache(url);
        //缓存没有就下载
        if(bitmap==null){
        imageView.setImageResource(R.drawable.progresstyle);
        }else {
            imageView.setImageBitmap(bitmap);
        }
    }
    public void cancelAllTasks(){
        if(mTask!=null){
            for(NewsimgAsyncTask task: mTask){
                task.cancel(false);
            }
        }
    }
    //用来加载从start到end的图片
    public void loadImages(int start,int end){
        for (int i = start; i < end; i++) {
            String url= LogimgAdapter.URLS[i];
            //从缓存中取图片
            Bitmap bitmap=getBitmapFromCache(url);
            //没有就下载
            if(bitmap==null){
                NewsimgAsyncTask task = new NewsimgAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else {
                ImageView imageView =(ImageView)mlistView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
    private class NewsimgAsyncTask extends AsyncTask<String,Void,Bitmap>{
        //private ImageView mimageView;
        private String murl;
        public NewsimgAsyncTask(String url){
            //mimageView=imageView;
            murl=url;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            String url=strings[0];
            //获取图片从网络
            Bitmap bitmap =getBitmapfromurl(url);
            if(bitmap!=null){
                //没有缓存的图片加入缓存
                addBitmapToCache(url,bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView =(ImageView)mlistView.findViewWithTag(murl);
            if(imageView!=null&bitmap!=null){
                imageView.setImageBitmap(bitmap);
            }
            mTask.remove(this);

        }
    }

}
