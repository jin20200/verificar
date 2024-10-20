package com.easyplexdemoapp.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.Device;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserAuthInfo implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("role")
    @Expose
    private String role;

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    @SerializedName("name")
    @Expose
    private String name;

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    @SerializedName("login_code")
    @Expose
    private String loginCode;



    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @SerializedName("verified")
    @Expose
    private int verified;

    @SerializedName("active")
    @Expose
    private int active;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @SerializedName("phone")
    @Expose
    private String phone;

    public UserAuthInfo() {

        //

    }


    public UserAuthInfo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        role = in.readString();
        name = in.readString();
        email = in.readString();
        avatar = in.readString();
        providerName = in.readString();
        type = in.readString();
        packName = in.readString();
        packId = in.readString();
        image = in.readString();
        transactionId = in.readString();
        startAt = in.readString();
        expiredIn = in.readString();
        if (in.readByte() == 0) {
            premuim = null;
        } else {
            premuim = in.readInt();
        }
        if (in.readByte() == 0) {
            manualPremuim = null;
        } else {
            manualPremuim = in.readInt();
        }
        emailVerifiedAt = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        message = in.readString();
        favoritesAnimes = in.createTypedArrayList(Media.CREATOR);
        favoritesSeries = in.createTypedArrayList(Media.CREATOR);
        favoritesStreaming = in.createTypedArrayList(Media.CREATOR);
        favoritesMovies = in.createTypedArrayList(Media.CREATOR);
    }

    public static final Creator<UserAuthInfo> CREATOR = new Creator<UserAuthInfo>() {
        @Override
        public UserAuthInfo createFromParcel(Parcel in) {
            return new UserAuthInfo(in);
        }

        @Override
        public UserAuthInfo[] newArray(int size) {
            return new UserAuthInfo[size];
        }
    };

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("provider_name")
    @Expose
    private String providerName;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("type")
    @Expose
    private String type;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("pack_name")
    @Expose
    private String packName;


    @SerializedName("pack_id")
    @Expose
    private String packId;


    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("transaction_id")
    @Expose
    private String transactionId;


    @SerializedName("start_at")
    @Expose
    private String startAt;

    @SerializedName("expired_in")
    @Expose
    private String expiredIn;

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    @SerializedName("premuim")
    @Expose
    private Integer premuim;


    @SerializedName("devices")
    @Expose
    private List<Device> deviceList = null;

    public Integer getManualPremuim() {
        return manualPremuim;
    }

    public void setManualPremuim(Integer manualPremuim) {
        this.manualPremuim = manualPremuim;
    }

    @SerializedName("manual_premuim")
    @Expose
    private Integer manualPremuim;


    @SerializedName("email_verified_at")
    @Expose
    private String emailVerifiedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;


    public List<Media> getFavoritesAnimes() {
        return favoritesAnimes;
    }

    public void setFavoritesAnimes(List<Media> favoritesAnimes) {
        this.favoritesAnimes = favoritesAnimes;
    }

    @SerializedName("favoritesAnimes")
    @Expose
    private List<Media> favoritesAnimes = null;





    @SerializedName("favoritesSeries")
    @Expose
    private List<Media> favoritesSeries = null;


    public List<Media> getFavoritesStreaming() {
        return favoritesStreaming;
    }

    public void setFavoritesStreaming(List<Media> favoritesStreaming) {
        this.favoritesStreaming = favoritesStreaming;
    }

    @SerializedName("favoritesStreaming")
    @Expose
    private List<Media> favoritesStreaming = null;


    public List<Media> getFavoritesSeries() {
        return favoritesSeries;
    }

    public void setFavoritesSeries(List<Media> latest)
    {
        this.favoritesSeries = latest;
    }

    @SerializedName("favoritesMovies")
    @Expose
    private List<Media> favoritesMovies = null;


    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    @SerializedName("profiles")
    @Expose
    private List<Profile> profiles = null;


    public List<Media> getFavoritesMovies() {
        return favoritesMovies;
    }

    public void setFavoritesMovies(List<Media> latest) {
        this.favoritesMovies = latest;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getPremuim() {
        return premuim;
    }

    public void setPremuim(Integer premuim) {
        this.premuim = premuim;
    }

    public Object getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }


    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getExpiredIn() {
        return expiredIn;
    }

    public void setExpiredIn(String expiredIn) {
        this.expiredIn = expiredIn;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(role);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(avatar);
        dest.writeString(providerName);
        dest.writeString(type);
        dest.writeString(packName);
        dest.writeString(packId);
        dest.writeString(image);
        dest.writeString(transactionId);
        dest.writeString(startAt);
        dest.writeString(expiredIn);
        if (premuim == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(premuim);
        }
        if (manualPremuim == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(manualPremuim);
        }
        dest.writeString(emailVerifiedAt);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(message);
        dest.writeTypedList(favoritesAnimes);
        dest.writeTypedList(favoritesSeries);
        dest.writeTypedList(favoritesStreaming);
        dest.writeTypedList(favoritesMovies);
    }
}