package com.easyplexdemoapp.data.model.collections;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class MediaCollection implements Parcelable {

    @SerializedName("id")
    private int id;


    @SerializedName("name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @SerializedName("poster_path")
    private String posterPath;


    @SerializedName("backdrop_path")
    private String backdropPath;

    protected MediaCollection(Parcel in) {
        id = in.readInt();
        name = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
    }

    public static final Creator<MediaCollection> CREATOR = new Creator<MediaCollection>() {
        @Override
        public MediaCollection createFromParcel(Parcel in) {
            return new MediaCollection(in);
        }

        @Override
        public MediaCollection[] newArray(int size) {
            return new MediaCollection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
    }
}
