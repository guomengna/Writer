package com.example.guome.writer.server;

import okhttp3.OkHttpClient;

/**
 * Created by guome on 2018/7/24.
 */

public class OKHttpUtils {
    private static final String BASE_URL = "http://192.168.170.1/";
    OkHttpClient client = null;

    private static OKHttpUtils ourInstance = null;

    public static OKHttpUtils getInstance() {

        if (ourInstance == null) {
            synchronized (OKHttpUtils.class) {
                if (ourInstance == null) {
                    ourInstance = new OKHttpUtils();
                }
            }
        }
        return ourInstance;
    }

    private OKHttpUtils() {
        client = new OkHttpClient();
    }
}

