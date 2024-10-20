package com.easyplexdemoapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings(RoomWarnings.DEFAULT_CONSTRUCTOR)
@Entity(tableName = "history", inheritSuperIndices = true)

public class History extends Media {


    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }


    @ColumnInfo(name = "user_deviceId")
    @Expose
    private String userNonAuthDeviceId;

    public String getUserNonAuthDeviceId() {
        return userNonAuthDeviceId;
    }

    public void setUserNonAuthDeviceId(String userNonAuthDeviceId) {
        this.userNonAuthDeviceId = userNonAuthDeviceId;
    }

    @ColumnInfo(name = "userprofile_history")
    @Expose
    private String userProfile;

    public int getUserMainId() {
        return userMainId;
    }

    public void setUserMainId(int userMainId) {
        this.userMainId = userMainId;
    }

    @SerializedName("userMainId")
    @Expose
    private int userMainId;

    @NonNull
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;
    @NonNull
    @SerializedName("tmdb_id")
    @Expose
    @ColumnInfo(name = "tmdbId_history")
    private String tmdbId;
    @ColumnInfo(name = "posterpath_history")
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @ColumnInfo(name = "serieName_history")
    @Expose
    private String serieName;
    @ColumnInfo(name = "title_history")
    @SerializedName("title")
    @Expose
    private String title;
    @ColumnInfo(name = "backdrop_path_history")
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @ColumnInfo(name = "link_history")
    @Expose
    private String link;
    @ColumnInfo(name = "tv_history")
    @Expose
    private String tv;
    @ColumnInfo(name = "type_history")
    @Expose
    private String type;
    @ColumnInfo(name = "positionEpisode_history")
    @Expose
    private String positionEpisode;
    @ColumnInfo(name = "externalId_history")
    @Expose
    private String externalId;
    @ColumnInfo(name = "seasonsNumber_history")
    @Expose
    private String seasonsNumber;
    @ColumnInfo(name = "seasondbId_history")
    @Expose
    private int seasondbId;
    @ColumnInfo(name = "mediaGenre_history")
    @Expose
    private String mediaGenre;
    @ColumnInfo(name = "seasonId_history")
    @Expose
    private String seasonsId;
    @ColumnInfo(name = "episodeNmber_history")
    @Expose
    private String episodeNmber;
    @ColumnInfo(name = "postion_history")
    @Expose
    private int position;
    @ColumnInfo(name = "episodeName_history")
    @Expose
    private String episodeName;
    @ColumnInfo(name = "currentSeasons_history")
    @Expose
    private String currentSeasons;
    @ColumnInfo(name = "episodeId_history")
    @Expose
    private String episodeId;
    @ColumnInfo(name = "serieId_history")
    @Expose
    private String serieId;
    @ColumnInfo(name = "episodeTmdb_history")
    @Expose
    private String episodeTmdb;

    public History(@NonNull String id, @NonNull String tmdbId, String posterPath, String title, String backdropPath, String link) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.link = link;
        this.title = title;
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
    @NonNull
    public String getTmdbId() {
        return tmdbId;
    }

    @Override
    public void setTmdbId(@NonNull String tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
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
    public String getLink() {
        return link;
    }

    @Override
    public void setLink(String link) {
        this.link = link;
    }

    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public String getPositionEpisode() {
        return positionEpisode;
    }

    public void setPositionEpisode(String positionEpisode) {
        this.positionEpisode = positionEpisode;
    }

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

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public int getSeasondbId() {
        return seasondbId;
    }

    public void setSeasondbId(int seasondbId) {
        this.seasondbId = seasondbId;
    }

    public String getMediaGenre() {
        return mediaGenre;
    }

    public void setMediaGenre(String mediaGenre) {
        this.mediaGenre = mediaGenre;
    }

    public String getEpisodeNmber() {
        return episodeNmber;
    }

    public void setEpisodeNmber(String episodeNmber) {
        this.episodeNmber = episodeNmber;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getCurrentSeasons() {
        return currentSeasons;
    }

    public void setCurrentSeasons(String currentSeasons) {
        this.currentSeasons = currentSeasons;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public String getEpisodeTmdb() {
        return episodeTmdb;
    }

    public void setEpisodeTmdb(String episodeTmdb) {
        this.episodeTmdb = episodeTmdb;
    }

    public String getSerieId() {
        return serieId;
    }

    public void setSerieId(String serieId) {
        this.serieId = serieId;
    }

    @Override
    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


}
