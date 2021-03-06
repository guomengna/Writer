package com.example.guome.writer.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import com.example.guome.writer.JavaBean.User;
import com.example.guome.writer.MainActivity;
import com.example.guome.writer.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
/**
 * Created by guome on 2017/10/9.
 */

public class IndexActivity extends Activity{
    private TextView welcome;
    private String ApplicationID="933f9a1decf27d18db673da059d2d861";
    private String currentUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.indexshow);
        //初始化BOMB
        Bmob.initialize(this, ApplicationID);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //获得当前缓存的用户，若是存在就不用再次登录了。
                // 获取保存的用户名和密码
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                if (sharedPreferences != null) {
                    currentUsername=sharedPreferences.getString("username", "");
                    Toast.makeText(IndexActivity.this, "sharedPreferences has value.currentUsername="+currentUsername,
                            Toast.LENGTH_SHORT).show();
                    if(!currentUsername.isEmpty()){
                        // 允许用户使用应用
                        Intent intent = new Intent(IndexActivity.this, MainActivity.class);
                        IndexActivity.this.startActivity(intent);
                    }else if(currentUsername.isEmpty()){
                        //缓存用户对象为空时， 可打开用户注册界面…
                        Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
                        IndexActivity.this.startActivity(intent);
                    }
                }else if(sharedPreferences==null){
                    Toast.makeText(IndexActivity.this, "sharedPreferences=null",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
                    IndexActivity.this.startActivity(intent);
                }
                IndexActivity.this.finish();
            }
        }, 3000); //三秒
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //缓存用户对象为空时， 可打开用户注册界面…
//                Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
//                IndexActivity.this.startActivity(intent);
//                IndexActivity.this.finish();
//            }
//        }, 3000); //三秒
    }
}
