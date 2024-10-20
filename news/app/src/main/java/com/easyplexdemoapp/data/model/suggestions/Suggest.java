package com.easyplexdemoapp.data.model.suggestions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Suggest implements Parcelable {


    @SerializedName("title")
    @Expose
    private String title;



    @SerializedName("message")
    @Expose
    private String message;


    protected Suggest(Parcel in) {
        title = in.readString();
        message = in.readString();
    }

    public static final Creator<Suggest> CREATOR = new Creator<Suggest>() {
        @Override
        public Suggest createFromParcel(Parcel in) {
            return new Suggest(in);
        }

        @Override
        public Suggest[] newArray(int size) {
            return new Suggest[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(message);
    }
}
