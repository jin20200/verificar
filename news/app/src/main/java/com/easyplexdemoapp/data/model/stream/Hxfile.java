package com.easyplexdemoapp.data.model.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hxfile {


    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("server_time")
    @Expose
    private String serverTime;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("result")
    @Expose
    private Result result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}
