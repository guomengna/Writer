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
}