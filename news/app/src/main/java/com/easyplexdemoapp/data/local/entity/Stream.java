package com.easyplexdemoapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;


@Entity( tableName = "stream",inheritSuperIndices = true)

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class Stream extends Media {

    public Stream(@NonNull String id, @NotNull String tmdbId , String posterPath, String title, String backdropPath, String link) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.backdropPath = backdropPath;
        this.link = link;
        this.tmdbId = tmdbId;
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


    @NonNull
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;

    @Override
    @NonNull
    public String getTmdbId() {
        return tmdbId;
    }

    @Override
    public void setTmdbId(@NonNull String tmdbId) {
        this.tmdbId = tmdbId;
    }

    @NonNull
    @SerializedName("tmdb_id")
    @ColumnInfo(name = "stream_tmdb")
    @Expose
    private String tmdbId;


    @Override
    public String getTitle() {
        return title;
    }



    @Override
    public void setTitle(String title) {
        this.title = title;
    }


    @ColumnInfo(name = "posterpath_stream")
    @SerializedName("poster_path")
    @Expose
    private String posterPath;



    @ColumnInfo(name = "title_stream")
    @SerializedName("title")
    @Expose
    private String title;

    @Override
    public String getBackdropPath() {
        return backdropPath;
    }

    @Override
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }


    @ColumnInfo(name = "backdrop_path_stream")
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public void setLink(String link) {
        this.link = link;
    }

    @ColumnInfo(name = "link_stream")
    @Expose
    private String link;


    @Override
    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }





}
