package com.easyplexdemoapp.data.model.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MediaStream {

    @SerializedName("episode_stream")
    @Expose
    private List<MediaStream> mediaStreams;

    public List<MediaStream> getMediaStreams() {
        return mediaStreams;
    }

    public MediaStream(String server, String link) {
        this.server = server;
        this.link = link;
    }

    public void setMediaStreams(List<MediaStream> mediaStreams) {
        this.mediaStreams = mediaStreams;
    }


    @SerializedName("name")
    @Expose
    private String name;

    public int getEmbed() {
        return embed;
    }

    public void setEmbed(int embed) {
        this.embed = embed;
    }




    @SerializedName("drmuuid")
    @Expose
    private String drmuuid;

    public String getDrmuuid() {
        return drmuuid;
    }

    public void setDrmuuid(String drmuuid) {
        this.drmuuid = drmuuid;
    }

    public String getDrmlicenceuri() {
        return drmlicenceuri;
    }

    public void setDrmlicenceuri(String drmlicenceuri) {
        this.drmlicenceuri = drmlicenceuri;
    }

    @SerializedName("drmlicenceuri")
    @Expose
    private String drmlicenceuri;

    public int getDrm() {
        return drm;
    }

    public void setDrm(int drm) {
        this.drm = drm;
    }

    @SerializedName("drm")
    @Expose
    private int drm;


    @SerializedName("useragent")
    @Expose
    private String useragent;

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @SerializedName("header")
    @Expose
    private String header;

    @SerializedName("server")
    @Expose
    private String server;
    @SerializedName("link")
    @Expose
    private String link;


    @SerializedName("linkpremuim")
    @Expose
    private int linkpremuim;

    public int getLinkpremuim() {
        return linkpremuim;
    }

    public void setLinkpremuim(int linkpremuim) {
        this.linkpremuim = linkpremuim;
    }


    public int getDownloadonly() {
        return downloadonly;
    }

    public void setDownloadonly(int downloadonly) {
        this.downloadonly = downloadonly;
    }

    @SerializedName("downloadonly")
    @Expose
    private int downloadonly;

    @SerializedName("embed")
    @Expose
    private int embed;


    public int getTorrent() {
        return torrent;
    }

    public void setTorrent(int torrent) {
        this.torrent = torrent;
    }

    @SerializedName("torrent")
    @Expose
    private int torrent;


    public int getYoutubeLink() {
        return youtubeLink;

    }





    public int getSupportedHosts() {
        return supportedHosts;
    }



    @SerializedName("supported_hosts")
    @Expose
    private int supportedHosts;

    public void setSupportedHosts(int supportedHosts) {
        this.supportedHosts = supportedHosts;
    }


    public void setYoutubeLink(int youtubeLink) {
        this.youtubeLink = youtubeLink;
    }


    @SerializedName("external")
    @Expose
    private int external;

    public int getExternal() {
        return external;
    }

    public void setExternal(int external) {
        this.external = external;
    }

    @SerializedName("youtubelink")
    @Expose
    private int youtubeLink;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("lang")
    @Expose
    private String lang;


    @SerializedName("type")
    @Expose
    private String type;

    public int getHls() {
        return hls;
    }

    public void setHls(int hls) {
        this.hls = hls;
    }

    @SerializedName("hls")
    @Expose
    private int hls;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return lang;
    }


    public String getMediaExtension() {
        return null;
    }

}