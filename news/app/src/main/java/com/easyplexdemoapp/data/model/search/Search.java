package com.easyplexdemoapp.data.model.search;

import android.os.Parcel;
import android.os.Parcelable;
import com.easyplexdemoapp.data.model.stream.MediaStream;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.easyplexdemoapp.data.model.genres.Genre;

import java.util.List;

public class Search implements Parcelable {

    @SerializedName("search")
    @Expose
    private List<Search> movies = null;


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("tmdb_id")
    @Expose
    private int tmdbId;


    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("preview_path")
    @Expose
    private String previewPath;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("hd")
    @Expose
    private Integer hd;
    @SerializedName("genreslist")
    @Expose
    private List<String> genreslist = null;
    @SerializedName("videos")
    @Expose
    private List<MediaStream> videos = null;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;


    protected Search(Parcel in) {
        movies = in.createTypedArrayList(Search.CREATOR);
        id = in.readString();
        tmdbId = in.readInt();
        name = in.readString();
        firstAirDate = in.readString();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        previewPath = in.readString();
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
        if (in.readByte() == 0) {
            voteCount = null;
        } else {
            voteCount = in.readInt();
        }
        if (in.readByte() == 0) {
            popularity = null;
        } else {
            popularity = in.readDouble();
        }
        if (in.readByte() == 0) {
            views = null;
        } else {
            views = in.readInt();
        }
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        releaseDate = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        if (in.readByte() == 0) {
            hd = null;
        } else {
            hd = in.readInt();
        }
        genreslist = in.createStringArrayList();
        genres = in.createTypedArrayList(Genre.CREATOR);
    }

    public static final Creator<Search> CREATOR = new Creator<Search>() {
        @Override
        public Search createFromParcel(Parcel in) {
            return new Search(in);
        }

        @Override
        public Search[] newArray(int size) {
            return new Search[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPreviewPath() {
        return previewPath;
    }

    public void setPreviewPath(String previewPath) {
        this.previewPath = previewPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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

    public Integer getHd() {
        return hd;
    }

    public void setHd(Integer hd) {
        this.hd = hd;
    }

    public List<String> getGenreslist() {
        return genreslist;
    }

    public void setGenreslist(List<String> genreslist) {
        this.genreslist = genreslist;
    }

    public List<MediaStream> getVideos() {
        return videos;
    }

    public void setVideos(List<MediaStream> videos) {
        this.videos = videos;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }


    public List<Search> getMovies() {
        return movies;
    }

    public void setMovies(List<Search> movies) {
        this.movies = movies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(movies);
        parcel.writeString(id);
        parcel.writeInt(tmdbId);
        parcel.writeString(name);
        parcel.writeString(firstAirDate);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(previewPath);
        if (voteAverage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(voteAverage);
        }
        if (voteCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(voteCount);
        }
        if (popularity == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(popularity);
        }
        if (views == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(views);
        }
        if (status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(status);
        }
        parcel.writeString(releaseDate);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        if (hd == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(hd);
        }
        parcel.writeStringList(genreslist);
        parcel.writeTypedList(genres);
    }
}
