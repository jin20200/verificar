package com.easyplexdemoapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Device {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("devices")
    @Expose
    private List<Device> deviceList = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("name")
    @Expose
    private String name;


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @SerializedName("model")
    @Expose
    private String model;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @SerializedName("created_at")
    @Expose
    private String createdAt;


    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("serial_number")
    @Expose
    private String serialNumber;

    @SerializedName("message")
    @Expose
    private String message;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}