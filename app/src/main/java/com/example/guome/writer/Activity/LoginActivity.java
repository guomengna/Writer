package com.example.guome.writer.Activity;

/**
 * Created by guome on 2017/9/25.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.guome.writer.JavaBean.User;
import com.example.guome.writer.MainActivity;
import com.example.guome.writer.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import com.example.guome.writer.MainActivity;

import static android.R.attr.tag;

/**
 *用Manager类登录，此类继承自BmobUser类
 *可以使用注册的邮箱地址登录
 * 登录成功，使用Manager表，用户名guomengna，密码na941209
 * Manager继承自BmobUser类，使用bmobtigong 的login接口
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private Button login;
    private TextView register;
    private TextView reset_psw;
    private EditText inputUsername;
    private EditText inputPassword;
    private long mExitTime;
    private String ApplicationID="933f9a1decf27d18db673da059d2d861";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        login = (Button) this.findViewById(R.id.login);
        register = (TextView) this.findViewById(R.id.register);
        inputUsername = (EditText) findViewById(R.id.inputUsername);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        reset_psw=(TextView)findViewById(R.id.reset_psw);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        reset_psw.setOnClickListener(this);
        Bmob.initialize(this, ApplicationID);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                managerLogin();
                break;
            case R.id.register:
                register();
                break;
//            case R.id.reset_psw:
//                Intent intent=new Intent(this,EmailChangePwd.class);
//                startActivity(intent);
//                break;
        }
    }

    public void managerLogin() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("登录中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        login.setEnabled(false);
        user.login(new SaveListener<BmobUser>() {//登录失败，为什么？？？
            @Override
            public void done(BmobUser user, BmobException e) {
                if(e==null){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    User currentUser = BmobUser.getCurrentUser(User.class);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
