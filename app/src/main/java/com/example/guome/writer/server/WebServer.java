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
    public static String path = "http://192.168.31.137:80";
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
                .url(path+"/easymanagement/getAllEasys")
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
                .url(path+"/easymanagement/getEasysByAuthor")
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
                .url(path+"/easymanagement/findByEasyId")
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
                .url(path+"/easymanagement/deleteEasyById")
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
                .url(path+"/easymanagement/addEasy")
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
                .url(path+"/usermanagement/login")
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
                .url(path+"/usermanagement/register")
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
                .url(path+"/usermanagement/getLoginUser")
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
                .url(path+"/usermanagement/validEmail")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 修改密码
     * @param id
     * @param newpassword
     * @param requestCallBack
     */
    public void changePassword(int id,String newpassword, okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("id", id+"" )
                .add("newpassword", newpassword )
                .build();

        Request request = new Request.Builder()
                .url(path+"/usermanagement/changePassword")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 获取所有的Email，查询是否已经有该邮箱
     * @param requestCallBack
     */
    public void getAllEmail(String email,okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("email", email )
                .build();

        Request request = new Request.Builder()
                .url(path+"/usermanagement/getAllEmail")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 获取所有的用户名,查询是否已经有该用户名
     * @param requestCallBack
     */
    public void getAllUsername(String username,okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("username", username )
                .build();

        Request request = new Request.Builder()
                .url(path+"/usermanagement/getAllUsername")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 获取文章数量
     * @param requestCallBack
     */
    public void getCountOfEasy(okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(path+"/easymanagement/getCountOfEasy")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 发布文章
     * @param title
     * @param content
     * @param author
     * @param publiceddata
     * @param requestCallBack
     */
    public void publicdEasy(String title, String content, String author, String publiceddata
            ,okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("title", title )
                .add("content", content )
                .add("author", author )
                .add("publiceddata", publiceddata )
                .build();

        Request request = new Request.Builder()
                .url(path+"/easymanagement/publicdEasy")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 根据文章的作者名称获取发布的文章
     * @param author
     * @param requestCallBack
     */
    public void getPublicedEasysByAuthor(String author,okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("author", author )
                .build();

        Request request = new Request.Builder()
                .url(path+"/easymanagement/getPublicedEasysByAuthor")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }

    /**
     * 根据Id获取发布的文章
     * @param publicedeasyid
     * @param requestCallBack
     */
    public void getPublicedEasysById(int publicedeasyid,okhttp3.Callback requestCallBack) {
        RequestBody body = new FormBody.Builder()
                .add("publicedeasyid", publicedeasyid+"" )
                .build();

        Request request = new Request.Builder()
                .url(path+"/easymanagement/findByPublicedEasyId")
                .post(body)
                .build();

        call = okHttpClient.newCall(request);
        call.enqueue(requestCallBack);
    }
}
