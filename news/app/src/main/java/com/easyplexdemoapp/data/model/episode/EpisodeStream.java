package com.easyplexdemoapp.data.model.episode;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.easyplexdemoapp.data.model.substitles.MediaSubstitle;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;


public class EpisodeStream extends BaseObservable {


    @SerializedName("episode_stream")
    @Expose
    private List<MediaSubstitle> streamepisode;

    public List<MediaSubstitle> getStreamepisode() {
        return streamepisode;
    }

    public void setStreamepisode(List<MediaSubstitle> streamepisode) {
        this.streamepisode = streamepisode;
    }


    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("episode_id")
    @Expose
    private String episodeId;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
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





    @SerializedName("server")
    @Expose
    private String server;

    @SerializedName("header")
    @Expose
    private String header;

    @SerializedName("useragent")
    @Expose
    private String useragent;

    public int getEmbed() {
        return embed;
    }

    public void setEmbed(int embed) {
        this.embed = embed;
    }

    @SerializedName("link")
    @Expose
    private String link;

    public int getYoutubeLink() {
        return youtubeLink;
    }


    public int getSupportedHosts() {
        return supportedHosts;
    }

    public void setSupportedHosts(int supportedHosts) {
        this.supportedHosts = supportedHosts;
    }

    @SerializedName("youtubelink")
    @Expose
    private int youtubeLink;

    public int getExternal() {
        return external;
    }

    public void setExternal(int external) {
        this.external = external;
    }

    @SerializedName("external")
    @Expose
    private int external;


    public void setYoutubeLink(int youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    @SerializedName("supported_hosts")
    @Expose
    private int supportedHosts;


    @SerializedName("embed")
    @Expose
    private int embed;

    public EpisodeStream(Integer id, String server, String link) {
        this.id = id;
        this.server = server;
        this.link = link;
    }

    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("hd")
    @Expose
    private String hd;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public int getHls() {
        return hls;
    }

    public void setHls(int hls) {
        this.hls = hls;
    }

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;


    @SerializedName("hls")
    @Expose
    private int hls;



    @BindingAdapter("android:imageUrlEP")

    public static void loadImageEP (ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
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

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @NonNull
    @Override
    public String toString() {

        return server;
    }
}