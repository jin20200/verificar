package com.easyplexdemoapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;



@Entity(tableName = "notifications")
public  class Notification {

    private int notificationId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "backdrop")
    private String backdrop;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "timestamp")
    private Date timestamp;

    @ColumnInfo(name = "type")
    private String type;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @ColumnInfo(name = "link")
    private String link;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "imdb")
    private String imdb;



    // Constructor
    public Notification() {

    }


    // Getters
    public int getNotificationId() { return notificationId; }
    public String getTitle() { return title; }
    public String getBackdrop() { return backdrop; }
    public String getOverview() { return overview; }
    public Date getTimestamp() { return timestamp; }
    public String getType() { return type; }
    public String getImdb() { return imdb; }

    // Setters
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }
    public void setTitle(String title) { this.title = title; }
    public void setBackdrop(String backdrop) { this.backdrop = backdrop; }
    public void setOverview(String overview) { this.overview = overview; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    public void setType(String type) { this.type = type; }
    public void setImdb(String imdb) { this.imdb = imdb; }
}