package com.easyplexdemoapp.data.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StripeStatus {

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @SerializedName("active")
    @Expose
    private Integer active;
}
