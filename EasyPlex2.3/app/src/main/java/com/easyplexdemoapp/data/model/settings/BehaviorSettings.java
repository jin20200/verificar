package com.easyplexdemoapp.data.model.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BehaviorSettings {


    @SerializedName("crash")
    @Expose
    private boolean crash;


    @SerializedName("force_update")
    @Expose
    private boolean forceUpdate;


    @SerializedName("hash256")
    @Expose
    private boolean hash256;


    @SerializedName("auth")
    @Expose
    private boolean auth;

    public boolean isCrash() {
        return crash;
    }

    public void setCrash(boolean crash) {
        this.crash = crash;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public boolean isHash256() {
        return hash256;
    }

    public void setHash256(boolean hash256) {
        this.hash256 = hash256;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @SerializedName("version")
    @Expose
    private String version;


}