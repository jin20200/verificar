package com.easyplexdemoapp.data.model.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {


    @SerializedName("id")
    @Expose
    private Integer id;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @SerializedName("user_name")
    @Expose
    private String user_name;


    @SerializedName("time")
    @Expose
    private String time;


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    @SerializedName("user_image")
    @Expose
    private String user_image;


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String name) {
        this.user_name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @SerializedName("user_id")
    @Expose
    private Integer userId;


    @SerializedName("comment")
    @Expose
    private String comment;
}
