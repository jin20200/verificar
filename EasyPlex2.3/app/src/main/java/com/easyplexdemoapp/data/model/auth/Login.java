package com.easyplexdemoapp.data.model.auth;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Login {


    @SerializedName("access_token")
    private String accessToken;


    @SerializedName("refresh_token")
    private String refresh;


    String message;
    Map<String, List<String>> errors;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }



    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

}
