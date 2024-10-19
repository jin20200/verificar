package com.easyplexdemoapp.data.model.ads;

import com.google.gson.annotations.SerializedName;

public class Ads {




    @SerializedName("id")
    private int id;

    @SerializedName("link")
    private String link;


    public int getCustomVast() {
        return customVast;
    }

    public void setCustomVast(int customVast) {
        this.customVast = customVast;
    }

    @SerializedName("customVast")
    private int customVast;





    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @SerializedName("duration")
    private String duration;


    @SerializedName("clickThroughUrl")
    private String clickThroughUrl;


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getClickThroughUrl() {
        return clickThroughUrl;
    }

    public void setClickThroughUrl(String clickThroughUrl) {
        this.clickThroughUrl = clickThroughUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
