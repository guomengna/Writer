package com.example.guome.writer.Activity;

/**
 * Created by guome on 2017/9/26.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.guome.writer.JavaBean.User;
import com.example.guome.writer.MainActivity;
import com.example.guome.writer.MyTool.CheckEmail;
import com.example.guome.writer.MyTool.MailValid;
import com.example.guome.writer.MyTool.MaxLengthWatcher;
import com.example.guome.writer.R;
import com.example.guome.writer.app.MyApplication;
import com.example.guome.writer.server.WebServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Response;

import java.util.Properties;
import java.util.UUID;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by guomengna on 2016/6/8.  注册成功实现
 * 开始的问题果然是出现在单选按钮上，单选按钮取值出现问题，set方法获取不到
 * 后来，出现TimeoutError，原因是我的手机没有联网
 * 用的Manager来注册，注策账户名zhangsan，姓名张三，手机13184116559，邮箱1577847631@qq.com，密码123456，性别男
 * 但是Uer表里不显示性别
 * 登录时可以用手机号、账户名、邮箱
 * 注册时手机号码、邮箱、用户名不能重复
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText register_inputUsername;
    private EditText register_inputPassword;
    private EditText register_inputEmail;
    private Button register_submit;
    private String str;
    private String username;//注册用户名
    private String password;//注册密码
    private String email;//注册邮箱
    private TextView title;
    private User newUser = new User();
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private CheckEmail checkEmail = new CheckEmail();
    private int generatedCode = 0;
    private int id = 0;
    private int code = 0;
    private User testUser=new User();
    private List<String> emailList=new ArrayList<String>();
    private List<String> usernameList=new ArrayList<String>();
    private String hasUsername="";
    private String hasEmail="";
    private int flag=0;
    private int flag1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        //增加访问web的权限，不推荐使用
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        register_submit = findViewById(R.id.register_submit);
        register_inputUsername = (EditText) findViewById(R.id.register_inputUsername);
        //调用限定最大位数方法，限定最多15位
        register_inputUsername.addTextChangedListener(new MaxLengthWatcher(15, register_inputUsername));
        register_inputPassword = (EditText) findViewById(R.id.register_inputPassword);
        //限定密码最多16位
        register_inputPassword.addTextChangedListener(new MaxLengthWatcher(16, register_inputPassword));

        register_inputEmail = (EditText) findViewById(R.id.register_inputEmail);

        register_submit = (Button) findViewById(R.id.register_submit);

        title = (TextView) findViewById(R.id.titleTv);
        title.setText("用户注册");
        //产生注册的验证码
        generatedCode = generateRandomNumber();
        findViewById(R.id.backa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(RegisterActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_submit:
                register();
                break;
        }
    }

    public void register() {
        progressDialog.setMessage("注册中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        //验证是否为空
        if (register_inputUsername.getText().toString().isEmpty() ||
                register_inputPassword.getText().toString().isEmpty() ||
                register_inputEmail.getText().toString().isEmpty()) {
            Toast.makeText(RegisterActivity.this, "请将注册信息填写完整", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        username = register_inputUsername.getText().toString();
        password = register_inputPassword.getText().toString();
        email = register_inputEmail.getText().toString();
        if (!isEmail(email)) {
            Toast.makeText(RegisterActivity.this, "地址格式错误", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        ifUsernameIsHad(username);
        //验证邮箱，有毛病……
//        Boolean yanzheng=new CheckEmail().isEmailValid(email);
//        Toast.makeText(RegisterActivity.this,"yanzheng="+yanzheng,Toast.LENGTH_LONG).show();
//        if(!yanzheng) {
//            Toast.makeText(RegisterActivity.this, "email地址不合法", Toast.LENGTH_SHORT).show();
//            progressDialog.dismiss();
//            return;
//        }
    }
    public void registerOperation(){
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setCode(generatedCode);
        newUser.setActived(false);
        try {
            WebServer.getWebServer().register(username, password, email, generatedCode,false, getRegisterCallBack);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * 注册的回调方法
     */
    okhttp3.Callback getRegisterCallBack = new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            final String ex = e.getMessage().toString();
            System.out.print("e=" + e);
            handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 progressDialog.dismiss();
                                 Toast.makeText(RegisterActivity.this,
                                         "注册失败,ex=" + ex, Toast.LENGTH_SHORT).show();
                             }
                         }
            );
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
            try {
                com.alibaba.fastjson.JSONObject getUser =jsonObject.getJSONObject("readerUser_returns");
                String re = jsonObject.getString("result");
                int r = Integer.parseInt(re);
                if (r == 1) {
                    System.out.print("注册成功");
                    int id = Integer.parseInt(getUser.getString("id"));
                    String username = getUser.getString("username");
                    String password = getUser.getString("password");
                    String email = getUser.getString("email");
                    //封装成User对象
                    User user = new User();
                    user.setUsername(username);
                    user.setId(id);
                    user.setPassword(password);
                    user.setEmail(email);
                    user.setCode(generatedCode);
                    user.setActived(false);
                    testUser = user;

                } else {
                    System.out.print("注册失败");
                }
            } catch (Exception e) {
                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    finish();
                    //注册信息成功之后验证邮箱
                    sendMail(testUser.getEmail().toString(),generatedCode,testUser.getId());
                    finish();
//                    Intent loginIntent = new Intent();
//                    loginIntent.setClass(RegisterActivity.this, LoginActivity.class);
//                    startActivity(loginIntent);
                }
            });
        }
    };

    //注册方法
//    public void register(){
//        username = register_inputUsername.getText().toString();
//        password = register_inputPassword.getText().toString();
//        //email = register_inputEmail.getText().toString();
////        if(!isEmail(email)){
////            Toast.makeText(RegisterActivity.this, "地址格式错误", Toast.LENGTH_SHORT).show();
////        }else{
////        }
//        newUser.setUsername(username);
//        newUser.setPassword(password);
//        //newUser.setEmail(email);
//        //验证是否为空
////        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)||TextUtils.isEmpty(str)) {
////            Toast.makeText(RegisterActivity.this, "请将注册信息填写完整", Toast.LENGTH_SHORT).show();
////            return;
////        }
//
//        //调用signUp接口注册
//        newUser.signUp(new SaveListener<User>() {
//            @Override
//            public void done(User s, BmobException e) {
//                if(e==null){
//                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
    /**
     * 判断邮箱格式
     */
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 发送激活邮件
     * @param to 收件人邮箱地址
     * @param code 激活码
     */
    public boolean sendMail(String to, int code, int id) {
        System.out.print("to="+to);
        System.out.print("code="+code);
        System.out.print("id="+id);
        try {
            Properties props = new Properties();
            props.put("username", "18202423540@163.com");
            props.put("password", "na19941209");
            props.put("mail.transport.protocol", "smtp" );
            props.put("mail.smtp.host", "smtp.163.com");
            props.put("mail.smtp.port", "25" );

            Session mailSession = Session.getDefaultInstance(props);

            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress("18202423540@163.com"));
            msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject("激活邮件");
            msg.setContent("<h1>此邮件为Writer官方激活邮件！请点击下面链接完成激活操作！" +
                    "</h1><h3><a href='http://192.168.1.111:80/usermanagement/validEmail?id="+id+"&code="+code+"'>"+
                    "http://192.168.1.111:80/usermanagement/validEmail</a></h3>","text/html;charset=UTF-8");
            msg.saveChanges();

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(props.getProperty("mail.smtp.host"), props
                    .getProperty("username"), props.getProperty("password"));
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * 生成6位的随机数，用于写在发送邮件的第二个参数上
     */
    public int generateRandomNumber(){
        int randomNumber = 0;
        int intFlag = (int)(Math.random() * 1000000);
        String flag = String.valueOf(intFlag);
        if (flag.length() == 6 && flag.substring(0, 1).equals("9"))
        {
        }
        else
        {
            intFlag = intFlag + 100000;
        }
        randomNumber=intFlag;
        System.out.println(randomNumber+"");
        return randomNumber;
    }

//    public void validEmail(){
//        try {
//            WebServer.getWebServer().validEmail(id,code,getvalidEmailCallBack);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 查询数据库中是否已经存在了相同的用户名或者邮箱
     *
     */
    public void ifUsernameIsHad(String username){
        try {
            WebServer.getWebServer().getAllUsername(username,getAllUsernameCallBack);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    /**
     * 验证Email是否存在
     *
     * @return
     */
    public void ifEmailIsHad(String email){
        try {
            WebServer.getWebServer().getAllEmail(email,getAllEmailCallBack);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    okhttp3.Callback getAllEmailCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 progressDialog.dismiss();
                                 Toast.makeText(RegisterActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                             }
                         }
            );
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try{
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
//                com.alibaba.fastjson.JSONArray getUser=jsonObject.getJSONArray("flag");
                int f=jsonObject.getInteger("flag");
                if(f==1) {
//                    for(int i=0;i<getUser.size();i++){
//                        emailList.add(getUser.get(i).toString());
                        hasEmail="邮箱通过";
                        flag1=1;
//                    }
                }else{
                    hasEmail="邮箱已经注册";
                }
            }catch(Exception e){
                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(flag1==1){
                                registerOperation();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this,"邮箱已经注册",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }
    };
    okhttp3.Callback getAllUsernameCallBack=new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            handler.post(new Runnable() {
                             @Override
                             public void run() {
                                 progressDialog.dismiss();
                                 Toast.makeText(RegisterActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                             }
                         }
            );
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {

            try{
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
//                com.alibaba.fastjson.JSONArray getUser=jsonObject.getJSONArray("resource");
//                String re=jsonObject.getString("flag");
                int f=jsonObject.getInteger("flag");
//                int r=Integer.parseInt(re);
                if(f==1) {
                    hasUsername="用户名通过";
                    flag=1;
                }else{
                    hasUsername="用户名已经存在";
                }
            }catch(Exception e){
                Log.e("exception", e.toString());
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(flag==1){
                        ifEmailIsHad(email);
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"用户名已经注册",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };
}
