package com.example.guome.writer.MyTool;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.guome.writer.JavaBean.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by guome on 2018/7/26.
 */

public class UseSharePerferences {
    static private Context context;
    // 获取SharedPreferences对象
    static SharedPreferences sharedPre = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

    /**
     * 使用saveLoginInfo保存登录的用户
     * @param context
     * @param user
     */
    public static void saveLoginInfo(Context context, User user) {
        // 获取Editor对象
        SharedPreferences.Editor editor = sharedPre.edit();
        // 设置参数
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putInt("id", user.getId());
        editor.putString("email", user.getEmail());
        // 提交
        editor.commit();
    }

    public static void deleteLoginInfo(){
        sharedPre.edit().clear().commit();
    }
}
