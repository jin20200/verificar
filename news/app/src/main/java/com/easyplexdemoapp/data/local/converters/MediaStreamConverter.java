package com.easyplexdemoapp.data.local.converters;

import androidx.room.TypeConverter;

import com.easyplexdemoapp.data.model.stream.MediaStream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * TypeConverter which persists MediaStream type into a known database type.
 *
 * @author Yobex.
 */

public class MediaStreamConverter {

    private MediaStreamConverter(){


    }



    @TypeConverter
    public static List<MediaStream> convertStringToList(String videoString) {
        Type listType = new TypeToken<ArrayList<MediaStream>>() {
        }.getType();
        return new Gson().fromJson(videoString, listType);
    }

    @TypeConverter
    public static String convertListToString(List<MediaStream> videos) {
        Gson gson = new Gson();
        return gson.toJson(videos);
    }

}
