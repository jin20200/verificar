package com.easyplexdemoapp.data.model.streaming;

public class IPTV {


    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setStreamLink(String streamLink) {
        this.streamLink = streamLink;
    }

    private String group;
    private String posterPath;
    private String streamLink;

    public boolean section = false;

    public IPTV(String title, String posterPath, String streamLink , boolean section) {
        this.title = title;
        this.posterPath = posterPath;
        this.streamLink = streamLink;
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getStreamLink() {
        return streamLink;
    }
}
