package com.example.guome.writer.Activity;

/**
 * Created by guome on 2017/9/26.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guome.writer.JavaBean.User;
import com.example.guome.writer.MyTool.MaxLengthWatcher;
import com.example.guome.writer.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by guomengna on 2016/6/8.  注册成功实现
 * 开始的问题果然是出现在单选按钮上，单选按钮取值出现问题，set方法获取不到
 * 后来，出现TimeoutError，原因是我的手机没有联网
 * 用的Manager来注册，注策账户名zhangsan，姓名张三，手机13184116559，邮箱1577847631@qq.com，密码123456，性别男
 * 但是Uer表里不显示性别
 * 登录时可以用手机号、账户名、邮箱
 * 注册时手机号码、邮箱、用户名不能重复
 */
public class RegisterActivity extends Activity {
    private EditText register_inputUsername;
    private EditText register_inputPassword;
    private EditText register_inputEmail;
    private Button register_submit;
    private String str ;
    private String username;
    private String password;
    private String email;
    private TextView title;
    private User newUser=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        register_inputUsername = (EditText) findViewById(R.id.register_inputUsername);
        //调用限定最大位数方法，限定最多15位
        register_inputUsername.addTextChangedListener(new MaxLengthWatcher(15, register_inputUsername));
        register_inputPassword = (EditText) findViewById(R.id.register_inputPassword);
        //限定密码最多16位
        register_inputPassword.addTextChangedListener(new MaxLengthWatcher(16, register_inputPassword));
        //限定手机号码最多11位
        register_inputEmail = (EditText) findViewById(R.id.register_inputEmail);
        //register_inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);//限制只能输入邮箱地址
        register_submit = (Button) findViewById(R.id.register_submit);
        //register_submit.getBackground().setAlpha(100);
        register_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        title = (TextView) findViewById(R.id.titleTv);
        title.setText("用户注册");
        findViewById(R.id.backa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //注册方法
    public void register(){
        username = register_inputUsername.getText().toString();
        password = register_inputPassword.getText().toString();
        //email = register_inputEmail.getText().toString();
//        if(!isEmail(email)){
//            Toast.makeText(RegisterActivity.this, "地址格式错误", Toast.LENGTH_SHORT).show();
//        }else{
//        }
        newUser.setUsername(username);
        newUser.setPassword(password);
        //newUser.setEmail(email);
        //验证是否为空
//        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)||TextUtils.isEmpty(str)) {
//            Toast.makeText(RegisterActivity.this, "请将注册信息填写完整", Toast.LENGTH_SHORT).show();
//            return;
//        }

        //调用signUp接口注册
        newUser.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                if(e==null){
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //判断邮箱格式
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
