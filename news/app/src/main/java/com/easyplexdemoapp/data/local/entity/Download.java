package com.easyplexdemoapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



@Entity(tableName = "download",inheritSuperIndices = true)
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class Download extends Media {


    public Download(@NonNull String id,String tmdbId,String backdropPath , String title , String link) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.backdropPath = backdropPath;
        this.title =title;
        this.link = link;
    }


    @ColumnInfo(name = "downloadDate")
    @Expose
    private String downloadDate;


    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

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
    public String getTmdbId() {
        return tmdbId;
    }

    @Override
    public void setTmdbId(@NonNull String tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public void setLink(String link) {
        this.link = link;
    }

    @NonNull
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;


    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    @SerializedName("tmdb_id")
    @Expose
    @ColumnInfo(name = "tmdbId_download",index = true)
    private String tmdbId;


    @ColumnInfo(name = "episodeId_download")
    @Expose
    private String episodeId;


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

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @ColumnInfo(name = "title_download")
    @SerializedName("title")
    @Expose
    private String title;

    public String getMediaGenre() {
        return mediaGenre;
    }

    public void setMediaGenre(String mediaGenre) {
        this.mediaGenre = mediaGenre;
    }

    @ColumnInfo(name = "mediaGenre_download")
    @Expose
    private String mediaGenre;


    @ColumnInfo(name = "backdropPath_download")
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;


    @ColumnInfo(name = "link_download")
    @Expose
    private String link;




    @ColumnInfo(name = "type_download")
    @Expose
    private String type;


    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    private String linkUrl;



    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }

    @ColumnInfo(name = "tv_history")
    @Expose
    private String tv;


    public String getPositionEpisode() {
        return positionEpisode;
    }

    public void setPositionEpisode(String positionEpisode) {
        this.positionEpisode = positionEpisode;
    }

    @ColumnInfo(name = "positionEpisode_history")
    @Expose
    private String positionEpisode;

    public String getSeasonsId() {
        return seasonsId;
    }

    public void setSeasonsId(String seasonsId) {
        this.seasonsId = seasonsId;
    }

    public String getSeasonsNumber() {
        return seasonsNumber;
    }

    public void setSeasonsNumber(String seasonsNumber) {
        this.seasonsNumber = seasonsNumber;
    }


    @ColumnInfo(name = "externalId_history")
    @Expose
    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @ColumnInfo(name = "seasonsNumber_history")
    @Expose
    private String seasonsNumber;

    @ColumnInfo(name = "seasonId_history")
    @Expose
    private String seasonsId;

    public String getEpisodeNmber() {
        return episodeNmber;
    }

    public void setEpisodeNmber(String episodeNmber) {
        this.episodeNmber = episodeNmber;
    }

    @ColumnInfo(name = "episodeNmber_history")
    @Expose
    private String episodeNmber;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @ColumnInfo(name = "postion_history")
    @Expose
    private int position;

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    @ColumnInfo(name = "episodeName_history")
    @Expose
    private String episodeName;


    public String getCurrentSeasons() {
        return currentSeasons;
    }

    public void setCurrentSeasons(String currentSeasons) {
        this.currentSeasons = currentSeasons;
    }

    @ColumnInfo(name = "currentSeasons_history")
    @Expose
    private String currentSeasons;

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    @ColumnInfo(name = "serieName_history")
    @Expose
    private String serieName;

    @ColumnInfo(name = "serieId_history")
    @Expose
    private String serieId;

    public String getEpisodeTmdb() {
        return episodeTmdb;
    }

    public void setEpisodeTmdb(String episodeTmdb) {
        this.episodeTmdb = episodeTmdb;
    }

    @ColumnInfo(name = "episodeTmdb_history")
    @Expose
    private String episodeTmdb;

    public String getSerieId() {
        return serieId;
    }

    public void setSerieId(String serieId) {
        this.serieId = serieId;
    }

}
