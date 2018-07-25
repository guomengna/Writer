package com.example.guome.writer.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guome.writer.JavaBean.Easy;
import com.example.guome.writer.MyTool.MyTextView;
import com.example.guome.writer.R;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.guome.writer.Activity.AddNewEasyActivity.getRealFilePath;

/**
 * Created by guome on 2017/12/7.
 */

public class EasyDetailActivity extends Activity implements View.OnClickListener {
    private String objectId;
    private EditText easy_editText;
    private int count = 0;
    private int firClick = 0;
    private int secClick = 0;
    private int flage = 0;
    ProgressDialog progressDialog;//进度显示框
    private String content;
    private String title;
    private Uri uri;
    private TextView submitTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.easy_detail_layout);
        objectId=getIntent().getStringExtra("objectId");
        easy_editText=findViewById(R.id.easy);
//        easy_editText.setEnabled(false);
        easy_editText.setFocusable(false);
        easy_editText.setOnClickListener(this);
        submitTextView=findViewById(R.id.submit);
        submitTextView.setOnClickListener(this);
        queryById();

    }
    public void queryById(){
        BmobQuery<Easy> query = new BmobQuery<Easy>();
        query.getObject(objectId, new QueryListener<Easy>() {

            @Override
            public void done(Easy object, BmobException e) {
                if(e==null){
                    //获得playerName的信息
                    easy_editText.setText(object.getContent());
                    Toast.makeText(EasyDetailActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EasyDetailActivity.this,"查询失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
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
        }

}


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
