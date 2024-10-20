package com.easyplexdemoapp.data.model.iptvplaylist;

import java.util.Date;

public class EpgProgram {
    private String channelId;
    private Date start;
    private Date stop;
    private String title;
    private String description;

    // Getters and setters
    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }
    public Date getStart() { return start; }
    public void setStart(Date start) { this.start = start; }
    public Date getStop() { return stop; }
    public void setStop(Date stop) { this.stop = stop; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
