package com.example.guome.writer.JavaBean;

/**
 * Created by guome on 2018/8/2.
 */

public class PublicedEasy {
    private String title;//发布文章的名称
    private String content;//发布文章的内容
    private String author;//发布文章的作者
    private String publiceddata;//发布日期
    private int publicedeasyid;//发布文章的id
    private int readcount;//阅读次数
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getPubliceddata() {
        return publiceddata;
    }
    public void setPubliceddata(String publiceddata) {
        this.publiceddata = publiceddata;
    }
    public int getPublicedeasyid() {
        return publicedeasyid;
    }
    public void setPublicedeasyid(int publicedeasyid) {
        this.publicedeasyid = publicedeasyid;
    }
    public int getReadcount() {
        return readcount;
    }
    public void setReadcount(int readcount) {
        this.readcount = readcount;
    }
}
