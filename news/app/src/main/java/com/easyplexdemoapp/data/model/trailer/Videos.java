package com.easyplexdemoapp.data.model.trailer;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yobex.
 */
public class Videos implements Parcelable {

    @SerializedName("id")
    @ColumnInfo(name = "video_id")
    private int id;

    @SerializedName("results")
    private List<Video> trailers = null;

    public Videos(int id, List<Video> trailers) {
        this.id = id;
        this.trailers = trailers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Video> trailers) {
        this.trailers = trailers;
    }

    protected Videos(Parcel in) {
        if (in.readByte() == 0x01) {
            trailers = new ArrayList<>();
            in.readList(trailers, Video.class.getClassLoader());
        } else {
            trailers = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (trailers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(trailers);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Videos> CREATOR = new Parcelable.Creator<Videos>() {
        @Override
        public Videos createFromParcel(Parcel in) {
            return new Videos(in);
        }

        @Override
        public Videos[] newArray(int size) {
            return new Videos[size];
        }
    };
}
