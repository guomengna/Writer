package com.example.guome.writer.server;

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
    public void getEasysByAuthor(okhttp3.Callback requestCallBack) {
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
}
