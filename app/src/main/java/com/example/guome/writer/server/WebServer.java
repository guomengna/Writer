package com.example.guome.writer.server;

import com.example.guome.writer.JavaBean.Easy;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import org.xutils.HttpManager;
import org.xutils.x;
/**
 * Created by guome on 2018/7/24.
 */

public class WebServer {
    OkHttpClient okHttpClient = new OkHttpClient();
    Call call;
    public static String path = "http://192.168.170.1/";
    HttpManager http = null;
    // 保证dclSingleton实例每次都是从主内存中取
    private volatile static WebServer webServer = null;
    private WebServer() {
        http = x.http();
    }

    public static WebServer getWebServer() {
        if (webServer == null) {
            synchronized (WebServer.class) {
                if (webServer == null) {
                    webServer = new WebServer();
                }
            }
        }
        return webServer;
    }

    /**
     * 获取所有文档
     * @param requestCallBack
     */
    public void getAllEasys(okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
//                .add("author", author + "")
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.111:80/easymanagement/getAllEasys")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 根据作者的姓名获取本作者的全部文章
     * @param author
     * @param requestCallBack
     */
    public void getEasysByAuthor(String author,okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("author", author + "")
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.111:80/easymanagement/getEasysByAuthor")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 按照文章的id查询某一篇文章
     * @param id
     * @param requestCallBack
     */
    public void findByEasyId(int id,okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("id", id + "")
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.111:80/easymanagement/findByEasyId")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 根据文章id删除一篇文章
     * @param
     * @param requestCallBack
     */
    public void deleteEasyById(int id,okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("id", id + "")
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.111:80/easymanagement/deleteEasyById")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     *
     * @param title
     * @param content
     * @param author
     * @param createData
     * @param updateData
     * @param requestCallBack
     */
    public void addEasy(String title,String content,String author,String createData,String updateData, okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("title", title )
                .add("content", content )
                .add("author", author )
                .add("createData", createData )
                .add("updateData", updateData )
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.111:80/easymanagement/addEasy")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 登录
     * @param username
     * @param password
     * @param requestCallBack
     */
    public void login(String username,String password, okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("username", username )
                .add("password", password )
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.111:80/usermanagement/login")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 新用户注册
     * @param username
     * @param password
     * @param email
     * @param requestCallBack
     */
    public void register(String username,String password, String email,int code, boolean actived, okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("username", username )
                .add("password", password )
                .add("email", email )
                .add("code", code+"" )
                .add("actived", actived+"" )
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.111:80/usermanagement/register")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 实际登录所用的接口
     * @param username
     * @param password
     * @param requestCallBack
     */
    public void getLoginUser(String username,String password, okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("username", username )
                .add("password", password )
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.111:80/usermanagement/getLoginUser")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 邮箱验证
     * @param id
     * @param code
     * @param requestCallBack
     */
    public void validEmail(int id,int code, okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("id", id+"" )
                .add("code", code+"" )
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.111:80/usermanagement/validEmail")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

}
