
package com.easyplexdemoapp.data.model.serie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.easyplexdemoapp.data.model.episode.Episode;

import java.util.List;

public class Season {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tmdb_id")
    @Expose
    private String tmdbId;

    @SerializedName("serie_id")
    @Expose
    private int serieId;

    @SerializedName("season_number")
    @Expose
    private String seasonNumber;

    public String getAnimeSeasonId() {
        return animeSeasonId;
    }

    public void setAnimeSeasonId(String animeSeasonId) {
        this.animeSeasonId = animeSeasonId;
    }

    @SerializedName("anime_season_id")
    @Expose
    private String animeSeasonId;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("overview")
    @Expose
    private Object overview;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("air_date")
    @Expose
    private String airDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("episodes")
    @Expose
    private List<Episode> episodes = null;


    public Season(Integer id, String tmdbId, int serieId, String seasonNumber, String name) {
        this.id = id;
        this.tmdbId = tmdbId;
        this.serieId = serieId;
        this.seasonNumber = seasonNumber;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(String tmdbId) {
        this.tmdbId = tmdbId;
    }

    public int getSerieId() {
        return serieId;
    }

    public void setSerieId(int serieId) {
        this.serieId = serieId;
    }

    public String getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getOverview() {
        return overview;
    }

    public void setOverview(Object overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
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

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }


    @Override
    public String toString() {
        return name;
    }
}
