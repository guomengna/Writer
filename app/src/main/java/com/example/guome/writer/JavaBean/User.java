package com.example.guome.writer.JavaBean;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by guome on 2017/9/25.
 */
public class User{
    private int id;
    private String username;
    private String password;
    private String email;
    private int code;//验证码
    private boolean actived;//激活状态

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    public boolean getActived(boolean actived) {
        return actived;
    }

}