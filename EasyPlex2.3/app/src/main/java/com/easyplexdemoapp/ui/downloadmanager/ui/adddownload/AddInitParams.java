/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package  EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

package com.easyplexdemoapp.ui.downloadmanager.ui.adddownload;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddInitParams implements Parcelable
{
    public String url;
    public String fileName;
    public String description;
    public String userAgent;
    public String refer;
    public Uri dirPath;
    public String type;
    public String mediaId;
    public String mediaName;
    public String mediabackdrop;

    @Nullable
    public Boolean unmeteredConnectionsOnly;
    @Nullable
    public Boolean retry;
    @Nullable
    public Boolean replaceFile;
    @Nullable
    public Integer numPieces;

    public AddInitParams() {}

    public AddInitParams(@NonNull Parcel source)
    {
        dirPath = source.readParcelable(Uri.class.getClassLoader());
        url = source.readString();
        fileName = source.readString();

        mediaId = source.readString();
        mediaName = source.readString();
        mediabackdrop = source.readString();
        type = source.readString();
        description = source.readString();
        userAgent = source.readString();
        byte unmeteredConnectionsOnlyVal = source.readByte();
        if (unmeteredConnectionsOnlyVal != -1) {
            unmeteredConnectionsOnly = unmeteredConnectionsOnlyVal > 0;
        }
        byte retryVal = source.readByte();
        if (retryVal != -1) {
            retry = retryVal > 0;
        }
        byte replaceFileVal = source.readByte();
        if (replaceFileVal != -1) {
            replaceFile = replaceFileVal > 0;
        }
        int numPiecesVal = source.readInt();
        if (numPiecesVal != -1) {
            numPieces = numPiecesVal;
        }
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(dirPath, flags);
        dest.writeString(url);
        dest.writeString(fileName);
        dest.writeString(mediaId);
        dest.writeString(mediaName);
        dest.writeString(mediabackdrop);
        dest.writeString(type);
        dest.writeString(description);
        dest.writeString(userAgent);
        if (unmeteredConnectionsOnly == null) {
            dest.writeByte((byte)-1);
        } else {
            dest.writeByte((byte)(unmeteredConnectionsOnly ? 1 : 0));
        }
        if (retry == null) {
            dest.writeByte((byte)-1);
        } else {
            dest.writeByte((byte)(retry ? 1 : 0));
        }
        if (replaceFile == null) {
            dest.writeByte((byte)-1);
        } else {
            dest.writeByte((byte)(replaceFile ? 1 : 0));
        }
        if (numPieces == null) {
            dest.writeInt(-1);
        } else {
            dest.writeInt(numPieces);
        }
    }

    public static final Creator<AddInitParams> CREATOR =
            new Creator<AddInitParams>()
            {
                @Override
                public AddInitParams createFromParcel(Parcel source)
                {
                    return new AddInitParams(source);
                }

                @Override
                public AddInitParams[] newArray(int size)
                {
                    return new AddInitParams[size];
                }
            };

    @Override
    public String toString()
    {
        return "AddInitParams{" +
                "url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", mediaId='" + mediaId + '\'' +
                ", mediabackdrop='" + mediabackdrop + '\'' +
                ", mediaName='" + mediaName + '\'' +
                ", description='" + description + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", dirPath=" + dirPath +
                ", unmeteredConnectionsOnly=" + unmeteredConnectionsOnly +
                ", retry=" + retry +
                ", replaceFile=" + replaceFile +
                ", numPieces=" + numPieces +
                '}';
    }
}
