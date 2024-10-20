package com.easyplexdemoapp.data.model.featureds;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Featured implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;


    @SerializedName("featured_id")
    @Expose
    private Integer featuredId;


    public Integer getCustom() {
        return custom;
    }

    public void setCustom(Integer custom) {
        this.custom = custom;
    }

    public String getCustomLink() {
        return customLink;
    }

    public void setCustomLink(String customLink) {
        this.customLink = customLink;
    }

    @SerializedName("custom")
    @Expose
    private Integer custom;

    @SerializedName("custom_link")
    @Expose
    private String customLink;


    @SerializedName("title")
    @Expose
    private String title;


    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    @SerializedName("quality")
    @Expose
    private String quality;

    public int getPremuim() {
        return premuim;
    }

    public void setPremuim(int premuim) {
        this.premuim = premuim;
    }

    @SerializedName("premuim")
    @Expose
    private int premuim;

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("vote_average")
    @Expose
    private float voteAverage;


    public int getEnableMiniposter() {
        return enableMiniposter;
    }

    public void setEnableMiniposter(int enableMiniposter) {
        this.enableMiniposter = enableMiniposter;
    }

    @SerializedName("enable_miniposter")
    @Expose
    private int enableMiniposter;


    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    public String getMiniposter() {
        return miniposter;
    }

    public void setMiniposter(String miniposter) {
        this.miniposter = miniposter;
    }

    @SerializedName("miniposter")
    @Expose
    private String miniposter;

    @SerializedName("type")
    @Expose
    private String type;


    @SerializedName("genre")
    @Expose
    private String genre;

    protected Featured(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            featuredId = null;
        } else {
            featuredId = in.readInt();
        }
        title = in.readString();
        posterPath = in.readString();
        type = in.readString();
        genre = in.readString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFeaturedId() {
        return featuredId;
    }

    public void setFeaturedId(Integer featuredId) {
        this.featuredId = featuredId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public static Creator<Featured> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (featuredId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(featuredId);
        }
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(type);
        dest.writeString(genre);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Featured> CREATOR = new Creator<Featured>() {
        @Override
        public Featured createFromParcel(Parcel in) {
            return new Featured(in);
        }

        @Override
        public Featured[] newArray(int size) {
            return new Featured[size];
        }
    };
}
