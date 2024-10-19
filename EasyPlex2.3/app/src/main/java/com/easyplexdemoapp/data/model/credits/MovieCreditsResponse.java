package com.easyplexdemoapp.data.model.credits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MovieCreditsResponse {

    @SerializedName("id")
    private Integer id;
    @SerializedName("cast")
    private List<Cast> casts;
    @SerializedName("crew")
    private List<Cast> crews;

    private Object tvrageId;

    public Object getTvrageId() {
        return tvrageId;
    }

    public void setTvrageId(Object tvrageId) {
        this.tvrageId = tvrageId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getInstagramId() {
        return instagramId;
    }

    public void setInstagramId(String instagramId) {
        this.instagramId = instagramId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    @SerializedName("facebook_id")
    @Expose
    private String facebookId;
    @SerializedName("instagram_id")
    @Expose
    private String instagramId;
    @SerializedName("twitter_id")
    @Expose
    private String twitterId;


    public MovieCreditsResponse(Integer id, List<Cast> casts, List<Cast> crews) {
        this.id = id;
        this.casts = casts;
        this.crews = crews;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }

    public List<Cast> getCrews() {
        return crews;
    }

    public void setCrews(List<Cast> crews) {
        this.crews = crews;
    }

}
