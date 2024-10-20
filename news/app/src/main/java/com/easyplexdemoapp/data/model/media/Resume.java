package com.easyplexdemoapp.data.model.media;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;


@Entity(primaryKeys = "tmdb",
        tableName = "resume"
)


public class Resume implements  Parcelable {


    public Resume(@NotNull String tmdb) {
        this.tmdb = tmdb;
    }


    protected Resume(Parcel in) {
        userResumeId = in.readInt();
        userMainId = in.readInt();
        userProfile = in.readString();
        userprofileResume = in.readString();
        tmdb = in.readString();
        deviceId = in.readString();
        if (in.readByte() == 0) {
            resumeWindow = null;
        } else {
            resumeWindow = in.readInt();
        }
        if (in.readByte() == 0) {
            resumePosition = null;
        } else {
            resumePosition = in.readInt();
        }
        if (in.readByte() == 0) {
            movieDuration = null;
        } else {
            movieDuration = in.readInt();
        }
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userResumeId);
        dest.writeInt(userMainId);
        dest.writeString(userProfile);
        dest.writeString(userprofileResume);
        dest.writeString(tmdb);
        dest.writeString(deviceId);
        if (resumeWindow == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(resumeWindow);
        }
        if (resumePosition == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(resumePosition);
        }
        if (movieDuration == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(movieDuration);
        }
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Resume> CREATOR = new Creator<Resume>() {
        @Override
        public Resume createFromParcel(Parcel in) {
            return new Resume(in);
        }

        @Override
        public Resume[] newArray(int size) {
            return new Resume[size];
        }
    };

    public void setTmdb(String tmdb) {
        this.tmdb = tmdb;
    }

    public Integer getResumeWindow() {
        return resumeWindow;
    }

    public void setResumeWindow(Integer resumeWindow) {
        this.resumeWindow = resumeWindow;
    }

    public Integer getResumePosition() {
        return resumePosition;
    }

    public void setResumePosition(Integer resumePosition) {
        this.resumePosition = resumePosition;
    }

    public Integer getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(Integer movieDuration) {
        this.movieDuration = movieDuration;
    }


    @SerializedName("user_resume_id")
    @Expose
    private int userResumeId;

    public int getUserMainId() {
        return userMainId;
    }

    public void setUserMainId(int userMainId) {
        this.userMainId = userMainId;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    @SerializedName("userMainId")
    @Expose
    private int userMainId;


    @ColumnInfo(name = "userprofile_history")
    @Expose
    private String userProfile;


    public String getUserprofileResume() {
        return userprofileResume;
    }

    public void setUserprofileResume(String userprofileResume) {
        this.userprofileResume = userprofileResume;
    }

    @ColumnInfo(name = "userprofile_resume")
    @Expose
    private String userprofileResume;

    @NonNull
    @SerializedName("tmdb")
    @Expose
    private String tmdb;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    public String getTmdb() {
        return tmdb;
    }

    @SerializedName("resumeWindow")
    @Expose
    private Integer resumeWindow;

    public static Creator<Resume> getCREATOR() {
        return CREATOR;
    }

    public int getUserResumeId() {
        return userResumeId;
    }

    public void setUserResumeId(int userResumeId) {
        this.userResumeId = userResumeId;
    }

    @SerializedName("resumePosition")
    @Expose
    private Integer resumePosition;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("movieDuration")
    @Expose
    private Integer movieDuration;


    @SerializedName("type")
    @Expose
    private String type;

}
