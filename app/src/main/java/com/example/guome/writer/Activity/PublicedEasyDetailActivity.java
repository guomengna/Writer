package com.example.guome.writer.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.JavaBean.PublicedEasy;
import com.example.guome.writer.R;
import com.example.guome.writer.server.WebServer;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by guome on 2018/8/26.
 */

public class PublicedEasyDetailActivity extends Activity{
    private int publicedeasyid;
    private String author;
    private String content;
    private String title;
    private TextView publicedEasyTitle;
    private TextView publicedEasyContent;
    private TextView publicedEasyAuthor;
    Handler handler=new Handler();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publicedeasy_detail_layout);
        publicedeasyid=Integer.parseInt(getIntent().getStringExtra("publicedEasyid"));
        publicedEasyTitle=findViewById(R.id.publicedeasy_title);
        publicedEasyContent=findViewById(R.id.publicedeasy_content);
        publicedEasyAuthor=findViewById(R.id.publicedeasy_author);
        progressDialog=new ProgressDialog(PublicedEasyDetailActivity.this);

        getPublicedEasyById();
    }

    public void getPublicedEasyById(){
        progressDialog.setMessage("获取中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        WebServer.getWebServer().getPublicedEasysById(publicedeasyid,getEasyByIdCallBack);
    }
    //获取该ID的文章，并把内容显示出来
    okhttp3.Callback getEasyByIdCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 progressDialog.dismiss();
                                 Toast.makeText(PublicedEasyDetailActivity.this, "获取失败",
                                         Toast.LENGTH_SHORT).show();
                             }
                         }
            );
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
            try{
                String re=jsonObject.getString("result");
                com.alibaba.fastjson.JSONObject getEasy=jsonObject.getJSONObject("getEasy_returns");
                int r=Integer.parseInt(re);
                if(r==1) {
                    publicedeasyid = Integer.parseInt(getEasy.getString("publicedeasyid"));
                    title = getEasy.getString("title");
                    content = getEasy.getString("content");
                    author = getEasy.getString("author");
//                    PublicedEasy publicedEasy = new PublicedEasy();
//                    publicedEasy.setContent(content);
//                    publicedEasy.setTitle(title);
//                    publicedEasy.setPublicedeasyid(publicedeasyid);
//                    publicedEasy.setAuthor(author);

                }
            }catch(Exception e){
                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    publicedEasyTitle.setText(title);
                    publicedEasyContent.setText(content);
                    publicedEasyAuthor.setText(author);
                    progressDialog.dismiss();
                    Toast.makeText(PublicedEasyDetailActivity.this, "获取完成",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}
