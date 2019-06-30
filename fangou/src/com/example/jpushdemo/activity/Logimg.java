package com.example.jpushdemo.activity;

public class Logimg {
    private int imgid;
    private int logid;
    private String imgpath;
    private String title;
    private String filetype;
    private String datetime;


    public Logimg() {
    }

    public Logimg(int imgid, int logid, String imgpath, String title,String filetype,String datetime) {
        this.imgid = imgid;
        this.logid = logid;
        this.imgpath = imgpath;
        this.title = title;
        this.filetype=filetype;
        this.datetime=datetime;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public int getLogid() {
        return logid;
    }

    public void setLogid(int logid) {
        this.logid = logid;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Logimg{" +
                "imgid=" + imgid +
                ", logid=" + logid +
                ", imgpath='" + imgpath + '\'' +
                ", title='" + title + '\'' +
                ", filetype='" + filetype + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
