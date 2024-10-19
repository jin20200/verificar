package com.easyplexdemoapp.data.model.report;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Report implements Parcelable {


    @SerializedName("title")
    @Expose
    private String title;



    @SerializedName("message")
    @Expose
    private String message;


    protected Report(Parcel in) {
        title = in.readString();
        message = in.readString();
    }

    public static final Creator<Report> CREATOR = new Creator<>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
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
