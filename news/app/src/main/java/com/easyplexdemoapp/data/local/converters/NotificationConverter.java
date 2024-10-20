package com.easyplexdemoapp.data.local.converters;

import androidx.room.TypeConverter;

import com.easyplexdemoapp.data.local.entity.Notification;
import com.easyplexdemoapp.data.model.certifications.Certification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class NotificationConverter {



    @TypeConverter
    public static List<Notification> fromString(String value) {
        Type listType = new TypeToken<List<Notification>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Notification> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }


    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}