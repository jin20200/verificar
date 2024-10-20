package com.easyplexdemoapp.data.model.languages;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * @author Yobex.
 */
public class Languages implements Parcelable {

    @SerializedName("id")
    private int id;


    @SerializedName("name")
    private String name;


    protected Languages(Parcel in) {
        id = in.readInt();
        name = in.readString();
        iso6391 = in.readString();
        englishName = in.readString();
    }

    public static final Creator<Languages> CREATOR = new Creator<>() {
        @Override
        public Languages createFromParcel(Parcel in) {
            return new Languages(in);
        }

        @Override
        public Languages[] newArray(int size) {
            return new Languages[size];
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

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    @SerializedName("iso_639_1")
    private String iso6391;


    @SerializedName("english_name")
    private String englishName;



    @SerializedName("logo_path")
    private String logoPath;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(iso6391);
        dest.writeString(englishName);
    }
}
