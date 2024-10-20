package com.easyplexdemoapp.data.local.converters;

import androidx.room.TypeConverter;
import com.easyplexdemoapp.data.model.substitles.MediaSubstitle;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/**
 * TypeConverter which persists Movie Substitle type into a known database type.
 *
 * @author Yobex.
 */
public class MediaSubstitlesConverter {


    private MediaSubstitlesConverter(){


    }

    @TypeConverter
    public static List<MediaSubstitle> convertStringToList(String castString) {
        String strSeparator = "__,__";
        String[] castArray = castString.split(strSeparator);
        List<MediaSubstitle> mediaSubstitles = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < castArray.length - 1; i++) {
            mediaSubstitles.add(gson.fromJson(castArray[i], MediaSubstitle.class));
        }
        return mediaSubstitles;
    }

    @TypeConverter
    public static String convertListToString(List<MediaSubstitle> substitles) {
        Gson gson = new Gson();
        return gson.toJson(substitles);
    }
}
