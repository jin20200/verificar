package com.easyplexdemoapp.data.model.iptvplaylist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistItem {
    private String title;
    private String streamUrl;
    private String tvgId;
    private String logoUrl;
    private List<EpgProgram> epgPrograms;
    private EpgProgram currentProgram;

    public PlaylistItem(String title, String streamUrl, String tvgId, String logoUrl) {
        this.title = title;
        this.streamUrl = streamUrl;
        this.tvgId = tvgId;
        this.logoUrl = logoUrl;
        this.epgPrograms = new ArrayList<>();
    }

    // Existing getters and setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getTvgId() {
        return tvgId;
    }

    public void setTvgId(String tvgId) {
        this.tvgId = tvgId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    // New EPG-related methods

    public List<EpgProgram> getEpgPrograms() {
        return epgPrograms;
    }

    public void setEpgPrograms(List<EpgProgram> epgPrograms) {
        this.epgPrograms = epgPrograms;
    }

    public EpgProgram getCurrentProgram() {
        return currentProgram;
    }

    public void setCurrentProgram(EpgProgram currentProgram) {
        this.currentProgram = currentProgram;
    }

}