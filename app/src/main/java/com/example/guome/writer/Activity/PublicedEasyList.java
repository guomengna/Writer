package com.example.guome.writer.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.guome.writer.Adapters.EasyListAdapter;
import com.example.guome.writer.Adapters.PublicedEasyListAdapter;
import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.JavaBean.PublicedEasy;
import com.example.guome.writer.R;
import com.example.guome.writer.server.WebServer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by guome on 2018/8/2.
 */

public class PublicedEasyList extends Activity implements View.OnClickListener{
    private ListView PublicedEasylistView;
    private ProgressDialog progressDialog;
    private String currentUsername;
    private Handler handler=new Handler();
    private List<PublicedEasy> publicedEasies=new ArrayList<>();
    private PublicedEasyListAdapter publicedEasyListAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publicec_easy_layout);
        PublicedEasylistView=findViewById(R.id.listView);
        progressDialog=new ProgressDialog(PublicedEasyList.this);
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        currentUsername=sharedPreferences.getString("username", "");
        init();
    }
    public void init(){
        progressDialog.setMessage("获取中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        try {
            WebServer.getWebServer().getPublicedEasysByAuthor(currentUsername,getPublicedEasysByAuthorCallBack);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    okhttp3.Callback getPublicedEasysByAuthorCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            });
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            List<PublicedEasy> easyList=new ArrayList<>();
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
            try{
                JSONArray getEasy_returns = new JSONArray(jsonObject.getString("publicedeasy_returns"));
                String re=jsonObject.getString("result");
                int r=Integer.parseInt(re);
                if(r==1) {
                    for (int i = 0; i < getEasy_returns.length(); i++) {
                        JSONObject jsonObject1 = getEasy_returns.getJSONObject(i);
                        //JSONObject jsonObject=jsonArray.getJSONObject(i);
                        //if(getEasy_returns!=null){
                        int publicedeasyid = Integer.parseInt(jsonObject1.getString("publicedeasyid"));
                        String title = jsonObject1.getString("title");
                        String content = jsonObject1.getString("content");
                        String publiceddata = jsonObject1.getString("publiceddata");
                        String author = jsonObject1.getString("author");

                        //封装成Easy对象
                        PublicedEasy publicedEasy = new PublicedEasy();
                        publicedEasy.setContent(content);
                        publicedEasy.setTitle(title);
                        publicedEasy.setPublicedeasyid(publicedeasyid);
                        publicedEasy.setPubliceddata(publiceddata);
                        publicedEasy.setAuthor(author);
                        System.out.println("easy title is" + title + " and content is " + content);
                        easyList.add(publicedEasy);
                    }
                    publicedEasies=easyList;
                }
            }catch(Exception e){
                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
//                    easyListAdapter.notifyDataSetChanged();
                    //初次显示列表数据需要初始化，与下拉刷新与上拉显示是不同的。
                    publicedEasyListAdapter = new PublicedEasyListAdapter(getApplication(),publicedEasies);
                    //将adapter添加到listview中
                    PublicedEasylistView.setAdapter(publicedEasyListAdapter);
                }
            });
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
