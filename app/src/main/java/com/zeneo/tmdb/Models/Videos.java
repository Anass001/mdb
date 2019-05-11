package com.zeneo.tmdb.Models;

public class Videos {

    private String img_url;
    private String title;
    private String vid_url;
    private String id;

    public Videos( String id, String img_url, String title, String vid_url ) {
        this.img_url = img_url;
        this.title = title;
        this.vid_url = vid_url;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVid_url() {
        return vid_url;
    }

    public void setVid_url(String vid_url) {
        this.vid_url = vid_url;
    }
}
