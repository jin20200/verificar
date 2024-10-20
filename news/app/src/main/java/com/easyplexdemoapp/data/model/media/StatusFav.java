package com.easyplexdemoapp.data.model.media;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusFav {


    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("password")
    @Expose
    private String password;


    @SerializedName("subscription")
    @Expose
    private String subscription;

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
