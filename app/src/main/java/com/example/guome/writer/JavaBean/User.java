package com.example.guome.writer.JavaBean;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by guome on 2017/9/25.
 */
public class User extends BmobUser {
    private String username;
    private String password;
    private String email;
    public void setUsername(String username){
        this.username=username;
    }
    public String getUsername(){
        return username;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getPassword(){
        return password;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail(){
        return email;
    }
}