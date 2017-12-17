package com.example.guome.writer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guome.writer.Activity.AddNewEasyActivity;
import com.example.guome.writer.Activity.EasyList;
import com.example.guome.writer.Activity.LiwenList;
import com.example.guome.writer.Activity.PersonInformationActivity;
import com.example.guome.writer.JavaBean.Easy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 主页
 */
public class MainActivity extends Activity implements Button.OnClickListener{
    CircleImageView touxiang;
    TextView zhuanlan;
    ImageView bianxie,tongbu;
    RelativeLayout enterLiwen,enterWenzhang;
    TextView counterEasy;
    private int counterOfEasy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        counterEasy=findViewById(R.id.number_wenzhang);
        queryEasyCount();
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
                Toast.makeText(MainActivity.this,"点击了专栏按钮",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        queryEasyCount();
    }
}
