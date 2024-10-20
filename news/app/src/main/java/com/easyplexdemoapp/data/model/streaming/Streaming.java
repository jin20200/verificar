package com.easyplexdemoapp.data.model.streaming;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Streaming {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @SerializedName("link")
    @Expose
    private String link;



    @SerializedName("status")
    @Expose
    private int status;


    @SerializedName("live")
    @Expose
    private int live;


    @SerializedName("vip")
    @Expose
    private int vip;


    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }


    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }
}
