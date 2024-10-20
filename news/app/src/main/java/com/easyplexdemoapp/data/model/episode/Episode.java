package com.easyplexdemoapp.data.model.episode;


import com.easyplexdemoapp.data.model.substitles.MediaSubstitle;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Episode {

    public boolean expanded = false;
    public boolean parent = false;

    public List<Episode> getGlobaldata() {
        return globaldata;
    }

    public void setGlobaldata(List<Episode> globaldata) {
        this.globaldata = globaldata;
    }

    public Episode(Integer id, Integer tmdbId, String seasonId, String episodeNumber, String name, String stillPath, String voteAverage) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.seasonId = seasonId;
        this.episodeNumber = episodeNumber;
        this.name = name;
        this.stillPath = stillPath;
        this.voteAverage = voteAverage;
    }

    @SerializedName("episodes")
    @Expose
    private List<Episode> episodes = null;


    public int getEnableStream() {
        return enableStream;
    }

    public void setEnableStream(int enableStream) {
        this.enableStream = enableStream;
    }

    public int getEnableMediaDownload() {
        return enableMediaDownload;
    }

    public void setEnableMediaDownload(int enableMediaDownload) {
        this.enableMediaDownload = enableMediaDownload;
    }

    @SerializedName("data")
    @Expose
    private List<Episode> globaldata = null;




    @SerializedName("enable_ads_unlock")
    @Expose
    private int enableAdsUnlock;


    @SerializedName("enable_stream")
    @Expose
    private int enableStream;

    public int getEnableAdsUnlock() {
        return enableAdsUnlock;
    }

    public void setEnableAdsUnlock(int enableAdsUnlock) {
        this.enableAdsUnlock = enableAdsUnlock;
    }

    @SerializedName("enable_media_download")
    @Expose
    private int enableMediaDownload;


    @SerializedName("anime_season_id")
    @Expose
    private String animeSeasonId;

    public String getanimeSeasonId() {
        return animeSeasonId;
    }

    public void setanimeseasonid(String animeseasonid) {
        this.animeSeasonId = animeseasonid;
    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tmdb_id")
    @Expose
    private Integer tmdbId;
    @SerializedName("season_id")
    @Expose
    private String seasonId;
    @SerializedName("episode_number")
    @Expose
    private String episodeNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("overview")
    @Expose
    private String overview;


    @SerializedName("still_path")
    @Expose
    private String stillPath;

    @SerializedName("vote_average")
    @Expose
    private String voteAverage;
    @SerializedName("vote_count")
    @Expose
    private String voteCount;
    @SerializedName("views")
    @Expose
    private String views;
    @SerializedName("air_date")
    @Expose
    private String airDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getSkiprecapStartIn() {
        return skiprecapStartIn;
    }

    public void setSkiprecapStartIn(Integer skiprecapStartIn) {
        this.skiprecapStartIn = skiprecapStartIn;
    }

    public Integer getHasrecap() {
        return hasrecap;
    }

    public void setHasrecap(Integer hasrecap) {
        this.hasrecap = hasrecap;
    }

    @SerializedName("skiprecap_start_in")
    @Expose
    private Integer skiprecapStartIn;

    public int getHls() {
        return hls;
    }

    public void setHls(int hls) {
        this.hls = hls;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    @SerializedName("hasrecap")
    @Expose
    private Integer hasrecap;



    @SerializedName("free")
    @Expose
    private int free;

    @SerializedName("hls")
    @Expose
    private int hls;


    @SerializedName("link")
    @Expose
    private String link;

    public List<EpisodeStream> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<EpisodeStream> downloads) {
        this.downloads = downloads;
    }

    @SerializedName("downloads")
    @Expose
    private List<EpisodeStream> downloads = null;


    @SerializedName("videos")
    @Expose
    private List<EpisodeStream> videos = null;


    @SerializedName("substitles")
    @Expose
    private List<MediaSubstitle> substitles = null;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
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

    public String getStillPath() {
        return stillPath;
    }

    public void setStillPath(String stillPath) {
        this.stillPath = stillPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
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

    public List<EpisodeStream> getVideos() {
        return videos;
    }

    public void setVideos(List<EpisodeStream> videos) {
        this.videos = videos;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public List<MediaSubstitle> getSubstitles() {
        return substitles;
    }

    public void setSubstitles(List<MediaSubstitle> substitles) {
        this.substitles = substitles;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}
