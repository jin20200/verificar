package com.easyplexdemoapp.data.local.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.easyplexdemoapp.data.model.credits.Cast;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * TypeConverter which persists Cast type into a known database type.
 *
 * @author Yobex.
 */
public class CastConverter {


    private CastConverter(){


    }


    @TypeConverter
    public static List<Cast> convertStringToList(String cast) {
        Type listType = new TypeToken<ArrayList<Cast>>() {
        }.getType();
        return new Gson().fromJson(cast, listType);
    }

    @TypeConverter
    public static String convertListToString(List<Cast> casts) {
        Gson gson = new Gson();
        return gson.toJson(casts);
    }
}
