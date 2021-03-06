package com.example.guome.writer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.guome.writer.Activity.AddNewEasyActivity;
import com.example.guome.writer.Activity.EasyList;
import com.example.guome.writer.Activity.LiwenList;
import com.example.guome.writer.Activity.LocalEasyList;
import com.example.guome.writer.Activity.LoginActivity;
import com.example.guome.writer.Activity.PersonInformationActivity;
import com.example.guome.writer.Activity.PublicedEasyList;
import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.JavaBean.User;
import com.example.guome.writer.app.MyApplication;
import com.example.guome.writer.server.WebServer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 主页
 */
public class MainActivity extends Activity implements Button.OnClickListener{
    CircleImageView touxiang;
    TextView zhuanlan;
    ImageView bianxie,tongbu;
    RelativeLayout enterLiwen,enterWenzhang,enterlocalwenzhang;
    TextView counterEasy,counterLocalEasy;
    private int counterOfEasy,counterofLocalEasy;
    private String ApplicationID="933f9a1decf27d18db673da059d2d861";
    private Handler handler=new Handler();
    private ProgressDialog progressDialog;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化BOMB
        Bmob.initialize(this, ApplicationID);
        touxiang= (CircleImageView) findViewById(R.id.touxiang);
        touxiang.setOnClickListener(this);
        zhuanlan=(TextView) findViewById(R.id.zhuanlan);
        zhuanlan.setOnClickListener(this);
        bianxie=(ImageView) findViewById(R.id.bianxie);
        bianxie.setOnClickListener(this);
        tongbu=(ImageView)findViewById(R.id.touxiang);
        tongbu.setOnClickListener(this);
        enterLiwen=findViewById(R.id.liwen_enter);
        enterLiwen.setOnClickListener(this);
        enterWenzhang=findViewById(R.id.wenzhang_enter);
        enterWenzhang.setOnClickListener(this);
        progressDialog=new ProgressDialog(MainActivity.this);
        counterEasy=findViewById(R.id.number_wenzhang);//上传成功的文章数量
        counterLocalEasy=findViewById(R.id.localnumber_wenzhang);//本地文章的数量
        enterlocalwenzhang=findViewById(R.id.localwenzhang_enter);//本地文章入口
        enterlocalwenzhang.setOnClickListener(this);//设置本地文章按钮动作
//        queryEasyCount();
         getEasyCount();//查询上传成功的文章数量，并显示出来
        queryLocalEasyCount();//查询本地文章数量，并显示出来
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.touxiang:

                Intent intent=new Intent();
                intent.setClass(MainActivity.this, PersonInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.zhuanlan:
                Intent intent2=new Intent();
                intent2.setClass(MainActivity.this, PublicedEasyList.class);
                startActivity(intent2);
//                Toast.makeText(MainActivity.this,"点击了专栏按钮",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bianxie:
                Toast.makeText(MainActivity.this,"点击了编写按钮",Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent();
                intent1.setClass(MainActivity.this, AddNewEasyActivity.class);
                startActivity(intent1);
                break;
            case R.id.tongbu:
                Toast.makeText(MainActivity.this,"点击了同步按钮",Toast.LENGTH_SHORT).show();
                break;
            case R.id.liwen_enter:
                Intent intentLiwenList=new Intent();
                intentLiwenList.setClass(MainActivity.this, LiwenList.class);
                startActivity(intentLiwenList);
                break;
            case R.id.wenzhang_enter:
                Intent intentEasyList=new Intent();
                intentEasyList.setClass(MainActivity.this, EasyList.class);
                startActivity(intentEasyList);
                break;
            case R.id.localwenzhang_enter:
                Intent intentLocalEasyList=new Intent();
                intentLocalEasyList.setClass(MainActivity.this, LocalEasyList.class);
                startActivity(intentLocalEasyList);
                break;
        }
    }

    public void queryEasyCount(){
        BmobQuery<Easy> query = new BmobQuery<Easy>();
        List<BmobQuery<Easy>> and = new ArrayList<BmobQuery<Easy>>();
        //大于00：00：00
        BmobQuery<Easy> q1 = new BmobQuery<Easy>();
        String start = "2015-05-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date  = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("createdAt",new BmobDate(date));
        and.add(q1);
        //小于23：59：59
        BmobQuery<Easy> q2 = new BmobQuery<Easy>();
        //hh表示12小时制，HH表示24小时制
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sDateFormat.format(new java.util.Date());
        String end = currentTime;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1  = null;
        try {
            date1 = sdf1.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("createdAt",new BmobDate(date1));
        and.add(q2);
        //添加复合与查询
        query.and(and);
        query.setLimit(500);
        query.findObjects(new FindListener<Easy>() {
            @Override
            public void done(List<Easy> object, BmobException e) {
                if (e == null) {
                    counterOfEasy=object.size();
                    //int类型转为字符串
                    counterEasy.setText(counterOfEasy+"");
                } else {
                    Toast.makeText(MainActivity.this, "获取文章数量失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //查询本地数据库中文章的数量，并显示出来
    public void queryLocalEasyCount(){
        //查询方法体
    }

    @Override
    protected void onResume() {
        super.onResume();
//        queryEasyCount();
        getEasyCount();
    }

    public int getEasyCount(){
        try {
            WebServer.getWebServer().getCountOfEasy(getEasyCountCallBack);
        } catch (android.net.ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    okhttp3.Callback getEasyCountCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try{
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
                String re=jsonObject.getString("result");
                int r=Integer.parseInt(re);
                if(r==1) {
                    System.out.print("获取成功");
                    count = jsonObject.getInteger("count");
                }else{
                    count = 0;
                }
            }catch(Exception e){
                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {

                    if(count==0){
                        counterEasy.setText("数量");
                    }else{
                        counterEasy.setText(count+"");
                    }
                }
            });
        }
    };
}
