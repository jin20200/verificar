package com.easyplexdemoapp.data.model.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Decrypter {


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @SerializedName("body")
    @Expose
    private String body;
}
