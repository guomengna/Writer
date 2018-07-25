package com.example.guome.writer.JavaBean;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by guome on 2017/10/11.
 */

public class Easy{
    private String content;
    private String title;
    private int id;
    private String author;
    private String createData;
    private String updateData;
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

    public void setCreateData(String createData) {
        this.createData = createData;
    }

    public void setUpdateData(String updateData) {
        this.updateData = updateData;
    }

    public String getCreateData() {
        return createData;
    }

    public String getUpdateData() {
        return updateData;
    }
}
