package com.easyplexdemoapp.data.local.converters;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.easyplexdemoapp.data.model.genres.Genre;
import java.lang.reflect.Type;
import java.util.List;


/**
 * TypeConverter which persists Genre type into a known database type.
 *
 * @author Yobex.
 */
public class GenreConverter {

    private GenreConverter(){

    }

    @TypeConverter
    public static List<Genre> fromString(String value) {
        Type listType = new TypeToken<List<Genre>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<Genre> genres) {
        Gson gson = new Gson();
        return gson.toJson(genres);
    }
}
