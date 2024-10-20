package com.easyplexdemoapp.data.remote;

public interface FirebaseCallback {
    void onSuccess(String apiUrl);
    void onFailure(Exception e);
}
