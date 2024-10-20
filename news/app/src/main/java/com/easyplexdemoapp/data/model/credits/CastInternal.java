package com.easyplexdemoapp.data.model.credits;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Yobex.
 */
public class CastInternal implements Parcelable {


    protected CastInternal(Parcel in) {
        castInternal = in.readParcelable(CastInternal.class.getClassLoader());
        castId = in.readInt();
        character = in.readString();
        creditId = in.readString();
        gender = in.readInt();
        id = in.readInt();
        name = in.readString();
        order = in.readInt();
        profilePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(castInternal, flags);
        dest.writeInt(castId);
        dest.writeString(character);
        dest.writeString(creditId);
        dest.writeInt(gender);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(order);
        dest.writeString(profilePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CastInternal> CREATOR = new Creator<CastInternal>() {
        @Override
        public CastInternal createFromParcel(Parcel in) {
            return new CastInternal(in);
        }

        @Override
        public CastInternal[] newArray(int size) {
            return new CastInternal[size];
        }
    };

    public CastInternal getCastInternal() {
        return castInternal;
    }

    public void setCastInternal(CastInternal castInternal) {
        this.castInternal = castInternal;
    }

    @SerializedName("cast")
    @Expose
    private CastInternal castInternal = null;

    @SerializedName("cast_id")
    private int castId;

    @SerializedName("character")
    private String character;

    @SerializedName("credit_id")
    private String creditId;

    @SerializedName("gender")
    private int gender;

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("order")
    private int order;

    @SerializedName("profile_path")
    private String profilePath;

    public CastInternal(int castId, String character, String creditId, int id, String name, int order, String profilePath) {
        this.castId = castId;
        this.character = character;
        this.creditId = creditId;
        this.id = id;
        this.name = name;
        this.order = order;
        this.profilePath = profilePath;
    }

    public int getCastId() {
        return castId;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

}