package com.easyplexdemoapp.data.local.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings(RoomWarnings.DEFAULT_CONSTRUCTOR)
@Entity(tableName = "seach_history", inheritSuperIndices = true)

public class AddedSearch extends Media {


    @Override
    @NonNull
    public String getId() {
        return id;
    }

    @Override
    public void setId(@NonNull String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getBackdropPath() {
        return backdropPath;
    }

    @Override
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @NonNull
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;


    @SerializedName("title")
    @Expose
    private String title;


    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;



}
