package com.example.jpushdemo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.xiao.songandroid.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LogimgAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private List<Logimg> logimgList;
    private LayoutInflater layoutInflater;
    private ImageLoader mIageLoader;
    private int mStart, mEnd;
    private Context context;
    public static String[] URLS;
    private boolean mFirstIn;
    private static int position;

    public LogimgAdapter(Context context, List<Logimg> data, ListView listView) {
        logimgList = data;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        mIageLoader = new ImageLoader(listView);
        URLS = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            URLS[i] = "http://www.song724.cn/img/mini/" + (data.get(i).getImgpath());
        }
        mFirstIn = true;
        listView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return logimgList.size();
    }

    @Override
    public Object getItem(int position) {
        return logimgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_layout, null);
            viewHolder.button = convertView.findViewById(R.id.btn_recover);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.tv_dellogimg);
            viewHolder.textTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_val);
            viewHolder.tvContent2 = (TextView) convertView.findViewById(R.id.tv_val2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Logimg value = logimgList.get(position);
        value.setLogid(position);
        viewHolder.button.setOnClickListener(listener);//添加点击事件\
        viewHolder.button.setTag(value);
        viewHolder.imageView.setImageResource(R.mipmap.ic_fangou);
        String url = "http://www.song724.cn/img/mini/" + logimgList.get(position).getImgpath();
        viewHolder.imageView.setTag(url);
        mIageLoader.showImageByAsyncTask(viewHolder.imageView,
                "http://www.song724.cn/img/mini/" + logimgList.get(position).getImgpath());
        viewHolder.textTitle.setText("日志:" + logimgList.get(position).getTitle() + " ID:" + logimgList.get(position).getLogid());
        viewHolder.tvContent.setText("图片:[" + logimgList.get(position).getImgpath() + "]");
        viewHolder.tvContent2.setText("过期时间:[" + logimgList.get(position).getDatetime() + "] 文件类型:"+logimgList.get(position).getFiletype());

        return convertView;
    }

    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            AlertDialog show = new AlertDialog.Builder(context)
                    .setTitle("确认")
                    .setMessage("确定吗？")
                    .setPositiveButton("恢复", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Toast.makeText(context, (CharSequence) logimgList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                            Logimg imgid = (Logimg)v.getTag();
                            logimgList.remove(imgid.getLogid());
                            LogimgAdapter.this.notifyDataSetChanged();
                            sendRequestWithOkHttp(String.valueOf(imgid.getImgid()));

                        }
                    })
                    .setNegativeButton("返回", null)
                    .show();
        }
    };

    private void sendRequestWithOkHttp(String imgid) {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder mBuild = new FormBody.Builder();
        mBuild.add("imgid", imgid);
        final RequestBody requestBodyPost = mBuild.build();

        //2.创建Request对象，设置一个url地址,设置请求方式。
        final Request request = new Request.Builder()
                .url("http://www.song724.cn/recoverimg")
                .post(requestBodyPost)
                .build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new Callback() {

            //请求失败执行的方法
            @Override
            public void onFailure(Call call, IOException e) {
                String err = e.getMessage().toString();
            }

            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String rtn = response.body().string();
                //Log.d("kwwl","response.body().string()=="+rtn);
                //获取返回码
                final String code = String.valueOf(response.code());
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            Toast.makeText(context,rtn, Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });
    }



    @Override
        public void onScrollStateChanged (AbsListView view,int scrollState){
            if (scrollState == SCROLL_STATE_IDLE) {
                //停止状态加载可见项
                mIageLoader.loadImages(mStart, mEnd);
            } else {
                //停止任务
                mIageLoader.cancelAllTasks();
            }
        }

        @Override
        public void onScroll (AbsListView view,int firstVisibleItem, int visibleItemCount,
        int totalItemCount){
            mStart = firstVisibleItem;
            mEnd = firstVisibleItem + visibleItemCount;

            //第一次加载图片使用
            if (mFirstIn && visibleItemCount > 0) {
                mIageLoader.loadImages(mStart, mEnd);
                mFirstIn = false;
            }
        }

        class ViewHolder {
            public TextView textTitle, tvContent, tvContent2;
            public ImageView imageView;
            public Button button;
        }


    }
