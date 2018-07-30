package com.example.guome.writer.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guome.writer.MainActivity;
import com.example.guome.writer.R;

import static com.example.guome.writer.MyTool.UseSharePerferences.deleteLoginInfo;

/**
 * Created by guome on 2017/9/19.
 */

public class PersonInformationActivity extends Activity implements Button.OnClickListener {
    private ImageButton fanhui;
    private Button logoutButton;
    private Button changePassword;
    private String username,email,sign;
    private TextView usernameTv,emailTv,signTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_informtion_layout);
//        final SharedPreferences sharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        fanhui = (ImageButton) findViewById(R.id.fanhui);
        logoutButton = findViewById(R.id.logout);
        changePassword = findViewById(R.id.changepasswor);
        usernameTv=findViewById(R.id.show_username);
        emailTv=findViewById(R.id.show_email);
        signTv=findViewById(R.id.show_sign);
        changePassword.setOnClickListener(this);
        fanhui.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        username=sharedPreferences.getString("username", "");
        email=sharedPreferences.getString("email", "");
        usernameTv.setText(username);
        emailTv.setText(email);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fanhui:
                Toast.makeText(PersonInformationActivity.this, "返回键被按了", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.logout:
                Toast.makeText(PersonInformationActivity.this, "点击退出登录按钮", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                Toast.makeText(PersonInformationActivity.this,
                        "删除成功", Toast.LENGTH_SHORT).show();
                finish();
                //缓存用户对象为空时， 可打开用户注册界面…
                Intent intent = new Intent(PersonInformationActivity.this, LoginActivity.class);
                PersonInformationActivity.this.startActivity(intent);
                break;
            case R.id.changepasswor:
                Intent intent1 = new Intent(PersonInformationActivity.this, ChangePasswordActivity.class);
                PersonInformationActivity.this.startActivity(intent1);
                break;
        }
    }
}
