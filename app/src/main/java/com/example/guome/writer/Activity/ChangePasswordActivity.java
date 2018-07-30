package com.example.guome.writer.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.guome.writer.JavaBean.User;
import com.example.guome.writer.MainActivity;
import com.example.guome.writer.R;
import com.example.guome.writer.app.MyApplication;
import com.example.guome.writer.server.WebServer;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by guome on 2018/7/29.
 */

public class ChangePasswordActivity extends Activity implements View.OnClickListener{
    private Button changePasswordBt;
    private EditText changePasswordEdittext1;
    private EditText changePasswordEdittext2;
    private String newPassword1="";
    private String newPassword2="";
    private Handler handler=new Handler();
    private ProgressDialog progressDialog;
    private String status="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword_layout);
        changePasswordBt=findViewById(R.id.changepassword_bt);
        changePasswordEdittext1=findViewById(R.id.new_1);
        changePasswordEdittext2=findViewById(R.id.new_2);
        changePasswordBt.setOnClickListener(this);
        progressDialog=new ProgressDialog(ChangePasswordActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changepassword_bt:
                changepassword();
                break;
        }
    }

    public void changepassword(){
        progressDialog.setMessage("修改中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        newPassword1=changePasswordEdittext1.getText().toString();
        newPassword2=changePasswordEdittext2.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        int userId=sharedPreferences.getInt("id",1);
        Toast.makeText(ChangePasswordActivity.this,"userId="+userId,
                Toast.LENGTH_LONG).show();
        if(newPassword1.equals(newPassword2)){
            try {
                WebServer.getWebServer().changePassword(userId,newPassword1,changepasswordUserCallBack);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            progressDialog.dismiss();
            Toast.makeText(ChangePasswordActivity.this,"两次输入的密码不相同",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }
    okhttp3.Callback changepasswordUserCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 progressDialog.dismiss();
                                 Toast.makeText(ChangePasswordActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
//                         Toast.makeText(LoginActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                             }
                         }
            );
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try{
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
//                com.alibaba.fastjson.JSONObject getUser=jsonObject.getJSONObject("readerUser_returns");
                String re=jsonObject.getString("result");
                int r=Integer.parseInt(re);
                if(r==1) {
                    System.out.print("修改成功");
                    status="修改成功";
                }else{
                    System.out.print("修改失败");
                    status="修改失败";
                }
            }catch(Exception e){
                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(ChangePasswordActivity.this,status,
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    };
}
