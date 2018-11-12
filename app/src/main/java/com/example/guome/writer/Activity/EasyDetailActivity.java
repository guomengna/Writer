package com.example.guome.writer.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.JavaBean.PublicedEasy;
import com.example.guome.writer.MyTool.MyTextView;
import com.example.guome.writer.R;
import com.example.guome.writer.server.WebServer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import okhttp3.Call;
import okhttp3.Response;

import static com.example.guome.writer.Activity.AddNewEasyActivity.getRealFilePath;

/**
 * Created by guome on 2017/12/7.
 */

public class EasyDetailActivity extends Activity implements View.OnClickListener {
    private String objectId;
    private int id;
    private EditText easy_editText;
    private int count = 0;
    private int firClick = 0;
    private int secClick = 0;
    private int flage = 0;
    private String content;
    private String title;
    private Uri uri;
    private TextView submitTextView;
    private Handler handler=new Handler();
    private Easy easyById=new Easy();
    private ImageButton back;
    private com.github.clans.fab.FloatingActionButton fabuBbutton;
    private com.github.clans.fab.FloatingActionButton rollbackButton;
    private ProgressDialog progressDialog;
    private Easy easyRecord;
    private PublicedEasy publicedEasy=new PublicedEasy();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easy_detail_layout);
//        objectId=getIntent().getStringExtra("objectId");
        id=Integer.parseInt(getIntent().getStringExtra("id"));
        easy_editText=findViewById(R.id.easy);
//        easy_editText.setEnabled(false);
        easy_editText.setFocusable(false);
        easy_editText.setOnClickListener(this);
        submitTextView=findViewById(R.id.submit);
        submitTextView.setOnClickListener(this);
        back=findViewById(R.id.fanhui);
        back.setOnClickListener(this);
        progressDialog=new ProgressDialog(EasyDetailActivity.this);
        fabuBbutton=findViewById(R.id.fabu_button);
        fabuBbutton.setOnClickListener(this);
        rollbackButton=findViewById(R.id.rollback);
        rollbackButton.setOnClickListener(this);
//        queryById();
        getEasyById();
    }
    public void getEasyById(){
        progressDialog.setMessage("获取中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        WebServer.getWebServer().findByEasyId(id,getEasyByIdCallBack);
    }
    //获取该ID的文章，并把内容显示出来
    okhttp3.Callback getEasyByIdCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handler.post(new Runnable() {
                             @Override
                             public void run() {
                             progressDialog.dismiss();
                             Toast.makeText(EasyDetailActivity.this, "登录失败",
                                     Toast.LENGTH_SHORT).show();
                             }
                         }
            );
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
            try{
//                JSONArray getEasy_returns = new JSONArray(jsonObject.getString("getEasy_returns"));
                String re=jsonObject.getString("result");
                com.alibaba.fastjson.JSONObject getEasy=jsonObject.getJSONObject("getEasy_returns");
                int r=Integer.parseInt(re);
                if(r==1) {
                        //JSONObject jsonObject=jsonArray.getJSONObject(i);
                        //if(getEasy_returns!=null){
                        int id = Integer.parseInt(getEasy.getString("id"));
                        String title = getEasy.getString("title");
                        String content = getEasy.getString("content");
                        String createData = getEasy.getString("createData");
                        String updateData = getEasy.getString("updateData");
                        String author = getEasy.getString("author");

                        //封装成Easy对象
                        Easy easy = new Easy();
                        easy.setContent(content);
                        easy.setTitle(title);
                        easy.setId(id);
                        easy.setCreateData(createData);
                        easy.setUpdateData(updateData);
                        easy.setAuthor(author);
                        System.out.println("easy title is" + title + " and content is " + content);
                        easy_editText.setText(easy.getContent().toString());
                        easyRecord=new Easy();
                        easyRecord=easy;
                    }
            }catch(Exception e){
                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            });
        }
    };
//    public void queryById(){
//        BmobQuery<Easy> query = new BmobQuery<Easy>();
//        query.getObject(objectId, new QueryListener<Easy>() {
//
//            @Override
//            public void done(Easy object, BmobException e) {
//                if(e==null){
//                    //获得playerName的信息
//                    easy_editText.setText(object.getContent());
//                    Toast.makeText(EasyDetailActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(EasyDetailActivity.this,"查询失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
    //双击编辑事件
    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(EasyDetailActivity.this,"双击方法",Toast.LENGTH_SHORT).show();
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            count++;
            if (count == 1) {
                firClick = (int) System.currentTimeMillis();
            } else if (count == 2) {
                secClick = (int) System.currentTimeMillis();
                switch (flage) {
                    case 0:
                        if (secClick - firClick < 1000) {// 双击事件,双击开始编辑
                            easy_editText.setEnabled(true);
                            easy_editText.setFocusable(true);
                            flage++;
                        }
                        count = 0;
                        firClick = 0;
                        secClick = 0;
                        return true;
                    case 1:
                        if (secClick - firClick < 1000) {// 双击事件，双击退出编辑并保存
                            easy_editText.setEnabled(false);
                            easy_editText.setFocusable(false);
                            easy_editText.getText();
                            flage--;
                            Toast.makeText(EasyDetailActivity.this,"编辑完成",Toast.LENGTH_SHORT).show();
                            submitOfChange();
                        }
                        count = 0;
                        firClick = 0;
                        secClick = 0;
                        return true;
                    default:
                        return true;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.easy:
                Toast.makeText(EasyDetailActivity.this,"点击",Toast.LENGTH_SHORT).show();
                easy_editText.setFocusable(true);

                firClick = (int) System.currentTimeMillis();
                secClick = (int) System.currentTimeMillis();
                if (secClick - firClick < 500&&flage==0) {
                    easy_editText.setFocusableInTouchMode(true);
                    easy_editText.requestFocus();
                    Toast.makeText(EasyDetailActivity.this, "这就是传说中的双击事件", Toast.LENGTH_SHORT).show();
                    easy_editText.setEnabled(true);
                    flage++;
                }
                break;
            case R.id.submit:
                content=easy_editText.getText().toString();
                submitOfChange();
                break;
            case R.id.fanhui:
                finish();
                break;
            case R.id.fabu_button:
                publicEasy();
                break;
            case R.id.rollback:
                Toast.makeText(EasyDetailActivity.this, "回滚", Toast.LENGTH_SHORT).show();
                break;
        }

}
    public void publicEasy(){
        publicedEasy.setAuthor(easyRecord.getAuthor());
        publicedEasy.setContent(easyRecord.getContent());
        publicedEasy.setTitle(easyRecord.getTitle());
        publicedEasy.setPubliceddata(getCurrentTime().toString());
        try {
            WebServer.getWebServer().publicdEasy(publicedEasy.getTitle(),publicedEasy.getContent(),
                    publicedEasy.getAuthor(),publicedEasy.getPubliceddata(),publicEasyCallBack);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    okhttp3.Callback publicEasyCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handler.post(new Runnable() {
                             @Override
                             public void run() {
                             progressDialog.dismiss();
                             Toast.makeText(EasyDetailActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                             }
                         }
            );
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
            try{
                String re=jsonObject.getString("result");
                int r=Integer.parseInt(re);
                if(r==1) {
                    System.out.print("发布成功");
                }else{
                    System.out.print("发布失败");
                }
            }catch(Exception e){
                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    finish();
                    Intent easyListIntent=new Intent();
                    easyListIntent.setClass(EasyDetailActivity.this,PublicedEasyList.class);
                    startActivity(easyListIntent);
                    Toast.makeText(EasyDetailActivity.this,
                            "发布成功", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    /**
     * 获取当前时间方法
     * @return
     */
    public String getCurrentTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String currentTime=df.format(new Date());
        return currentTime;
    }

    //更新方法还没有实现……还没有想好要不要回滚，还是直接覆盖之前的版本
    public void submitOfChange(){
            Toast.makeText(EasyDetailActivity.this,"点击了完成按钮",Toast.LENGTH_SHORT).show();
            progressDialog = new ProgressDialog(EasyDetailActivity.this);
            progressDialog.show();
            //收集文章的数据
            if(content.length()>5){
                //检测是否有换行，若有且换行时字符数量少于5则截止到换行为止
                String titleTemp=content.substring(0,5);
                for(int i=0;i<titleTemp.length();i++){
                    if(titleTemp.substring(i,i+1).equals("\n")){
                        Toast.makeText(EasyDetailActivity.this,"第"+i+"个是换行字符",Toast.LENGTH_SHORT).show();
                        title=content.substring(0,i);
                        Toast.makeText(EasyDetailActivity.this,"title是"+title,Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }else{
                title=content;
            }

            Easy easy=new Easy();
            easy.setContent(content);
            easy.setTitle(title);
//            easy.update(objectId, new UpdateListener() {
//                @Override
//                public void done(BmobException e) {
//                    if(e==null){
//                        progressDialog.dismiss();
//                        Toast.makeText(EasyDetailActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
//                        //提交图片的方法，放在文章上传成功之中
//                        //图片路径
//                        String picPath = getRealFilePath(EasyDetailActivity.this,uri);
//                        String picstr="storage/1EF9-1702/DCIM/Camera/IMG_20171028_175422.jpg";
//                        File file =new File(picPath);
//                        BmobFile bmobFile = new BmobFile(file);//停在这一步，为什么？？？
//                        bmobFile.uploadblock(new UploadFileListener() {
//                            @Override
//                            public void done(BmobException e) {
//                                if(e==null){
//                                    Toast.makeText(EasyDetailActivity.this,"图片上传成功",Toast.LENGTH_SHORT).show();
//                                }else{
//                                    Toast.makeText(EasyDetailActivity.this,"图片上传失败",Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            @Override
//                            public void onProgress(Integer value) {
//                                // 返回的上传进度（百分比）
//                            }
//                        });
//                    }else{
//                        progressDialog.dismiss();
//                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                        Toast.makeText(EasyDetailActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
    }

}
