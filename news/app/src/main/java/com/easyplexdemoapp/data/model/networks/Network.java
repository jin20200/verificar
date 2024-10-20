package com.easyplexdemoapp.data.model.networks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author Yobex.
 */
public class Network implements Parcelable {

    @SerializedName("id")
    private int id;


    @SerializedName("name")
    private String name;

    protected Network(Parcel in) {
        id = in.readInt();
        name = in.readString();
        logoPath = in.readString();
        originCountry = in.readString();
    }

    public static final Creator<Network> CREATOR = new Creator<Network>() {
        @Override
        public Network createFromParcel(Parcel in) {
            return new Network(in);
        }

        @Override
        public Network[] newArray(int size) {
            return new Network[size];
        }
    };

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

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    @SerializedName("logo_path")
    private String logoPath;


    @SerializedName("origin_country")
    private String originCountry;

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(logoPath);
        dest.writeString(originCountry);
    }
}
