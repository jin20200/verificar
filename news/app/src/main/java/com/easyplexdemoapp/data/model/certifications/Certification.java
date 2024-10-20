package com.easyplexdemoapp.data.model.certifications;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * @author Yobex.
 */
public class Certification implements Parcelable {


    @SerializedName("id")
    private int id;


    @SerializedName("country_code")
    private String countryCode;

    protected Certification(Parcel in) {
        id = in.readInt();
        countryCode = in.readString();
        certification = in.readString();
        meaning = in.readString();
    }

    public static final Creator<Certification> CREATOR = new Creator<Certification>() {
        @Override
        public Certification createFromParcel(Parcel in) {
            return new Certification(in);
        }

        @Override
        public Certification[] newArray(int size) {
            return new Certification[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @SerializedName("certification")
    private String certification;


    @SerializedName("meaning")
    private String meaning;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(countryCode);
        dest.writeString(certification);
        dest.writeString(meaning);
    }
}