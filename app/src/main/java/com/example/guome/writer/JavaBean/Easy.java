package com.example.guome.writer.JavaBean;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by guome on 2017/10/11.
 */

public class Easy extends BmobObject{
    private String content;
    private String title;
    private int id;
    private String author;
    public void setContent(String content) {
        this.content = content;
    }
    public void setTitle(String title){
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void setCreatedAt(String createdAt) {
        super.setCreatedAt(createdAt);
    }

    @Override
    public void setUpdatedAt(String updatedAt) {
        super.setUpdatedAt(updatedAt);
    }

    @Override
    public void setObjectId(String objectId) {
        super.setObjectId(objectId);
    }

    @Override
    public String getCreatedAt() {
        return super.getCreatedAt();
    }

    @Override
    public String getObjectId() {
        return super.getObjectId();
    }

    @Override
    public String getUpdatedAt() {
        return super.getUpdatedAt();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }
}
