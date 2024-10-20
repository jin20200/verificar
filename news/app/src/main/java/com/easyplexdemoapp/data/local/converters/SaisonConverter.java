package com.easyplexdemoapp.data.local.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.easyplexdemoapp.data.model.serie.Season;
import java.util.ArrayList;
import java.util.List;

/**
 * TypeConverter which persists Seasons type into a known database type.
 *
 * @author Yobex.
 */
public class SaisonConverter {


    private SaisonConverter(){


    }

    @TypeConverter
    public static List<Season> convertStringToList(String castString) {
        String strSeparator = "__,__";
        String[] castArray = castString.split(strSeparator);
        List<Season> casts = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < castArray.length - 1; i++) {
            casts.add(gson.fromJson(castArray[i], Season.class));
        }
        return casts;
    }

    @TypeConverter
    public static String convertListToString(List<Season> seasons) {
        Gson gson = new Gson();
        return gson.toJson(seasons);
    }
}
