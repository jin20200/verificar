package com.easyplexdemoapp.data.model.substitles;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImdbLangs {

    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("english_name")
    @Expose
    private String englishName;
    @SerializedName("name")
    @Expose
    private String name;

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @NonNull
    @Override
    public String toString() {
        return englishName;
    }
}
