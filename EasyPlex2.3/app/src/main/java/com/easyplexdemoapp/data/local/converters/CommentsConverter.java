package com.easyplexdemoapp.data.local.converters;

import androidx.room.TypeConverter;

import com.easyplexdemoapp.data.model.comments.Comment;
import com.easyplexdemoapp.data.model.trailer.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * TypeConverter which persists Video type into a known database type.
 *
 * @author Yobex.
 */
public class CommentsConverter {

    private CommentsConverter(){


    }

    @TypeConverter
    public static List<Comment> convertStringToList(String videoString) {
        Type listType = new TypeToken<List<Comment>>() {
        }.getType();
        return new Gson().fromJson(videoString, listType);
    }


    @TypeConverter
    public static String convertListToString(List<Comment> comments) {
        Gson gson = new Gson();
        return gson.toJson(comments);
    }
}
