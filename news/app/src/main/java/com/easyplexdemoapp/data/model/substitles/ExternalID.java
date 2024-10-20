package com.easyplexdemoapp.data.model.substitles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalID {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;

    public ExternalID(Integer id, String imdbId, String facebookId, String instagramId, String twitterId) {
        this.id = id;
        this.imdbId = imdbId;
        this.facebookId = facebookId;
        this.instagramId = instagramId;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
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

}
