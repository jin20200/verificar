package com.easyplexdemoapp.data.local.converters;

import androidx.room.TypeConverter;

import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.certifications.Certification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CertificationsConverter {

    @TypeConverter
    public static List<Certification> fromString(String value) {
        Type listType = new TypeToken<List<Certification>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Certification> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
