package com.easyplexdemoapp.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;


/**
 * @author Yobex.
 */


@Entity(tableName = "animes",inheritSuperIndices = true)
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
public class Animes extends Media {

    public Animes(@NonNull String id , @NotNull String tmdbId, String posterPath, String name) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.posterPath = posterPath;
        this.name = name;
    }


    @NonNull
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;


    @SerializedName("tmdb_id")
    @Expose
    @ColumnInfo(name = "anime_tmdb_id")
    private String tmdbId;

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
    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @ColumnInfo(name = "series_posterPath")
    @SerializedName("poster_path")
    @Expose
    private String posterPath;


    @ColumnInfo(name = "series_name")
    @SerializedName("name")
    @Expose
    private String name;


    @Override
    @NonNull
    public String getId() {
        return id;
    }

    @Override
    public void setId(@NonNull String id) {
        this.id = id;
    }


}
