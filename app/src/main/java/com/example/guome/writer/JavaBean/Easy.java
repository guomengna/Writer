package com.example.guome.writer.JavaBean;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by guome on 2017/10/11.
 */

public class Easy extends BmobObject{
//    private String objectId;
    private String content;
//    private String[] picPaths;
    private String title;

    public void setContent(String content) {
        this.content = content;
    }
    public void setTitle(String title){
        this.title=title;
    }

    public String getTitle() {
        return title;
    }
    //    public void setObjectId(String objectId) {
//        this.objectId = objectId;
//    }

//    public void setPicPaths(String[] picPaths) {
//        this.picPaths = picPaths;
//    }
//
//    public String[] getPicPaths() {
//        return picPaths;
//    }

//    public String getobjectId() {
//        return objectId;
//    }

    public String getContent() {
        return content;
    }
}
